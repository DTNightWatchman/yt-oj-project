package com.yt.ytojcodesandbox;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.WordTree;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.github.dockerjava.core.exec.StatsCmdExec;
import com.yt.ytojcodesandbox.model.ExecuteCodeRequest;
import com.yt.ytojcodesandbox.model.ExecuteCodeResponse;
import com.yt.ytojcodesandbox.model.ExecuteMessage;
import com.yt.ytojcodesandbox.model.JudgeInfo;
import com.yt.ytojcodesandbox.utils.ProcessUtils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

public class JavaDockerCodeSandbox extends JavaCodeSandboxTemplate {

    private static final String GLOBAL_CODE_DIR_NAME = "tmpcode";

    private static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";

    public static final int TIME_OUT = 5000;


    public static void main(String[] args) throws InterruptedException {
        JavaDockerCodeSandbox javaDockerCodeSandbox = new JavaDockerCodeSandbox();
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setInputList(Arrays.asList("1 2", "2 3"));
        String code = ResourceUtil.readStr("testcode/simplecomputeargs/Main.java", StandardCharsets.UTF_8);

        executeCodeRequest.setCode(code);
        executeCodeRequest.setLanguage("java");
        ExecuteCodeResponse executeCodeResponse = javaDockerCodeSandbox.executeCode(executeCodeRequest);
        System.out.println(executeCodeResponse);
    }

    /**
     * 使用容器执行代码
     * @param userCodeFile
     * @param inputList
     * @return
     */
    @Override
    public List<ExecuteMessage> runCode(File userCodeFile, List<String> inputList) {
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
        // 创建容器，把文件复制到容器中
        // 获取默认的 Docker Client
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
        // 拉取镜像
        String image = "openjdk:8-alpine";

        CreateContainerCmd containerCmd = dockerClient.createContainerCmd(image);

        HostConfig hostConfig = new HostConfig();
        hostConfig.setBinds(new Bind(userCodeParentPath, new Volume("/root/oj-project/app")));
        hostConfig.withMemory(100 * 1000 * 1000L);
        hostConfig.withMemorySwap(0L);
        hostConfig.withCpuCount(1L);

        CreateContainerResponse createContainerResponse = containerCmd
                .withHostConfig(hostConfig)
                .withNetworkDisabled(true)
                .withReadonlyRootfs(true)
                .withAttachStdin(true)
                .withAttachStderr(true)
                .withAttachStdout(true)
                .withTty(true)
                .exec();
        System.out.println(createContainerResponse);
        String containerId = createContainerResponse.getId();
        dockerClient.startContainerCmd(containerId).exec();

        // 执行代码得到输出结果
        List<ExecuteMessage> executeMessageList = new ArrayList<>();

        final long[] maxMemory = {0L};
        Object lock = new Object(); // 创建一个对象用于同步
        StatsCmd statsCmd = dockerClient.statsCmd(containerId);

        statsCmd.exec(new ResultCallback<Statistics>() {
            @Override
            public void onNext(Statistics statistics) {
                System.out.println("内存占用:" + statistics.getMemoryStats().getUsage());
                maxMemory[0] = Math.max(statistics.getMemoryStats().getUsage(), maxMemory[0]);
                synchronized (lock) {
                    lock.notify();
                }
            }

            @Override
            public void onStart(Closeable closeable) {
            }

            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onComplete() {
            }

            @Override
            public void close() throws IOException {
            }
        });

        for (String inputArgs : inputList) {
            StopWatch stopWatch = new StopWatch();
            // docker 启动容器 75e1e07f6ad393d141c2f767a328109dda8c07f98f3b244ea3502e853f73ab0c
            // docker exec awesome_noether java -cp /root/oj-project/app Main 1 3
            String[] inputArgsArray = inputArgs.split(" ");
            String[] cmdArray = ArrayUtil.append(new String[]{"java", "-cp", "/root/oj-project/app", "Main"}, inputArgsArray);

            ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(containerId)
                    .withCmd(cmdArray)
                    .withAttachStdin(true)
                    .withAttachStdout(true)
                    .withAttachStderr(true)
                    .exec();
            System.out.println("创建执行命令:" + execCreateCmdResponse.toString());

            ExecuteMessage executeMessage = new ExecuteMessage();
            final String[] message = {null};
            final String[] errorMessage = {null};
            long time = 0L;
            final boolean[] timeout = {true};
            String execId = execCreateCmdResponse.getId();

            try {
                stopWatch.start();

                dockerClient.execStartCmd(execId).exec(new ExecStartResultCallback() {
                    @Override
                    public void onNext(Frame frame) {
                        StreamType streamType = frame.getStreamType();
                        if (StreamType.STDERR.equals(streamType)) {
                            errorMessage[0] = new String(frame.getPayload());
                            System.out.println("输出错误结果:" + errorMessage[0]);
                        } else {
                            message[0] = new String(frame.getPayload());
                            System.out.println("输出结果:" + message[0]);
                        }
                        super. onNext(frame);
                    }

                    @Override
                    public void onComplete() {
                        timeout[0] = false;
                        super.onComplete();
                    }
                }).awaitCompletion(TIME_OUT, TimeUnit.MICROSECONDS);
                stopWatch.stop();
                time = stopWatch.getLastTaskTimeMillis();
                statsCmd.close();
            } catch (InterruptedException e) {
                System.out.println("程序执行异常");
                throw new RuntimeException(e);
            }
            executeMessage.setMessage(message[0]);
            executeMessage.setErrorMessage(errorMessage[0]);
            executeMessage.setTime(time);

            // 加锁阻塞，等待回调执行完
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            executeMessage.setMemory(maxMemory[0]);
            maxMemory[0] = 0L;
            executeMessageList.add(executeMessage);
        }
        return executeMessageList;
    }

}
