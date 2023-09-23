package com.yt.ytojcodesandbox;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.yt.ytojcodesandbox.model.ExecuteCodeRequest;
import com.yt.ytojcodesandbox.model.ExecuteCodeResponse;
import com.yt.ytojcodesandbox.model.ExecuteMessage;
import com.yt.ytojcodesandbox.model.JudgeInfo;
import com.yt.ytojcodesandbox.utils.ProcessUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @Author: YT
 * @Description: java模板方法
 * @DateTime: 2023/9/21$ - 21:39
 */
@Slf4j
public abstract class JavaCodeSandboxTemplate implements CodeSandbox {


    private static final String GLOBAL_CODE_DIR_NAME = "tmpcode";

    private static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";

    private static final long TIME_OUT = 5000L;

    private static final String SECURITY_MANAGER_PATH = "E:\\gitee\\yt-oj-project\\ytoj-code-sandbox\\src\\main\\resources\\security";

    public static final String SECURITY_MANAGER_CLASS_NAME = "MySecurityManager";


    /**
     * 1. 把用户代码保存为文件
     * @param code 用户代码
     * @return
     */
    public File saveCodeToFile(String code) {
        String userDir = System.getProperty("user.dir");
        String globalCodePathName = userDir + File.separator + GLOBAL_CODE_DIR_NAME ;

        if (!FileUtil.exist(globalCodePathName)) {
            FileUtil.mkdir(globalCodePathName);
        }

        // 用户代码隔离存放
        String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();
        String globalCodePathPath = userCodeParentPath + File.separator + GLOBAL_JAVA_CLASS_NAME;
        return FileUtil.writeString(code, globalCodePathPath, StandardCharsets.UTF_8);
    }

    /**
     * 2. 编译代码
     * @param userCodeFile
     * @return
     */
    public ExecuteMessage compileFile(File userCodeFile) {
        String compileCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsolutePath());
        try {
            Process process = Runtime.getRuntime().exec(compileCmd);
            ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(process, "编译");
            if (executeMessage.getExitValue() != 0) {
                throw new RuntimeException("编译错误");
            }
            return executeMessage;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 3. 执行代码得到输出结果
     * @param userCodeFile
     * @param inputList
     * @return
     */
    public List<ExecuteMessage> runCode(File userCodeFile, List<String> inputList) {
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        int ifLongTime = 0;
        for (String inputArgs : inputList) {
            String runCmd = String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s;%s -Djava.security.manager=%s Main %s",
                    userCodeParentPath, SECURITY_MANAGER_PATH, SECURITY_MANAGER_CLASS_NAME, inputArgs);
            try {
                // 需要处理程序运行超时的问题
                Process process = Runtime.getRuntime().exec(runCmd);
                // 超时控制
                FutureTask<Integer> futureTask = new FutureTask<>(() -> {
                    try {
                        Thread.sleep(TIME_OUT);
                        if (process.isAlive()) {
                            process.destroy();
                            System.out.println("超时了，中断");
                            // 有一个中断就不执行后面可能有问题的输入了
                            return 1;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return 0;
                });
                new Thread(futureTask).start();
                Integer res = futureTask.get();
                if (res == 1) {
                    ifLongTime = 1;
                    break;
                }
                ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(process, "运行");
                System.out.println(executeMessage);
                executeMessageList.add(executeMessage);

            } catch (IOException | InterruptedException | ExecutionException e) {
                throw new RuntimeException("程序执行异常");
            }
        }
        if (ifLongTime == 1) {
            throw new RuntimeException("代码执行时间过长");
        }
        return executeMessageList;
    }

    /**
     * 得到响应结果
     * @param executeMessageList
     * @return
     */
    public ExecuteCodeResponse getOutputResponse(List<ExecuteMessage> executeMessageList) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();

        List<String> outputList  = new ArrayList<>();
        long maxTime = 0L; // 取最大值判断是否超时
        for (ExecuteMessage executeMessageItem : executeMessageList) {
            String errorMessage = executeMessageItem.getErrorMessage();
            if (StrUtil.isNotBlank(errorMessage)) {
                // 执行中存在错误
                executeCodeResponse.setMessage(errorMessage);
                // todo 抽象出公共包后改为枚举
                executeCodeResponse.setStatus(3);
                break;
            }
            outputList.add(executeMessageItem.getMessage());
            Long itemTime = executeMessageItem.getTime();
            if (itemTime != null) {
                maxTime = Math.max(itemTime, maxTime);
            }
        }
        // 正常执行完
        if (outputList.size() == executeMessageList.size()) {
            executeCodeResponse.setStatus(1);
        }
        executeCodeResponse.setStatus(1);
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setTime(maxTime);
        // todo 从process中拿到运行内存比较复杂
        // judgeInfo.setMemory();
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }

    /**
     * 进行文件清理
     * @param userCodeFile
     * @return
     */
    public boolean deleteFile(File userCodeFile) {
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
        if (userCodeFile.getParentFile() != null) {
            boolean del = FileUtil.del(userCodeParentPath);
            System.out.println("删除" + (del ? "成功" : "失败"));
            return del;
        }
        return true;
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {

        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();

        String language = executeCodeRequest.getLanguage();

        // 1. 将用户代码保存为文件
        File userCodeFile = saveCodeToFile(code);

        // 编译代码，得到class文件
        ExecuteMessage compileFileExecuteMessage = compileFile(userCodeFile);
        System.out.println(compileFileExecuteMessage);

        // 执行代码，得到输出结果
        List<ExecuteMessage> executeMessageList = runCode(userCodeFile, inputList);

        // 收集整理输出结果
        ExecuteCodeResponse executeCodeResponse = getOutputResponse(executeMessageList);

        // 进行文件清理
        boolean b = deleteFile(userCodeFile);
        if (!b) {
            log.error("delete file error, userCodeFilePath = {}", userCodeFile.getAbsolutePath());
        }
        return executeCodeResponse;
    }

    /**
     * 获取错误的返回结果
     * @param throwable
     * @return
     */
    private ExecuteCodeResponse getErrorResponse(Throwable throwable) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(new ArrayList<>());
        executeCodeResponse.setMessage(throwable.getMessage());
        // 表示代码沙箱错误
        executeCodeResponse.setStatus(2);
        executeCodeResponse.setJudgeInfo(new JudgeInfo());
        return executeCodeResponse;
    }
}
