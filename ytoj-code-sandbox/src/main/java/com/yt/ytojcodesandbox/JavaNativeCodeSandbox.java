package com.yt.ytojcodesandbox;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.WordTree;
import com.yt.ytojcodesandbox.model.ExecuteCodeRequest;
import com.yt.ytojcodesandbox.model.ExecuteCodeResponse;
import com.yt.ytojcodesandbox.model.ExecuteMessage;
import com.yt.ytojcodesandbox.model.JudgeInfo;
import com.yt.ytojcodesandbox.security.DefaultSecurityManage;
import com.yt.ytojcodesandbox.utils.ProcessUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

@Component
public class JavaNativeCodeSandbox implements CodeSandbox{

    private static final String GLOBAL_CODE_DIR_NAME = "tmpcode";

    private static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";

    private static final long TIME_OUT = 50000L;

    private static final List<String> blackList = Arrays.asList("Files", "exec");

    private static final WordTree wordTree = new WordTree();

    private static final String SECURITY_MANAGER_PATH = "E:\\gitee\\yt-oj-project\\ytoj-code-sandbox\\src\\main\\resources\\security";

    public static final String SECURITY_MANAGER_CLASS_NAME = "MySecurityManager";

    static {
        WordTree wordTree = new WordTree();
        wordTree.addWords(blackList);
    }

    public static void main(String[] args) {
        JavaNativeCodeSandbox javaNativeCodeSandbox = new JavaNativeCodeSandbox();
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setInputList(Arrays.asList("abc\ndef", "def\nefg"));
        String code = ResourceUtil.readStr("testcode/simplecomputeargs/Main.java", StandardCharsets.UTF_8);

        executeCodeRequest.setCode(code);
        executeCodeRequest.setLanguage("java");
        ExecuteCodeResponse executeCodeResponse = javaNativeCodeSandbox.executeCode(executeCodeRequest);
        System.out.println(executeCodeResponse);
    }


    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();

        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();

        FoundWord foundWord = wordTree.matchWord(code);

        if (foundWord != null) {
            executeCodeResponse.setMessage("含恶意代码");
            executeCodeResponse.setStatus(3);
            return executeCodeResponse;
        }

        // todo 语言
        String language = executeCodeRequest.getLanguage();

        String userDir = System.getProperty("user.dir");
        String globalCodePathName = userDir + File.separator + GLOBAL_CODE_DIR_NAME ;

        if (!FileUtil.exist(globalCodePathName)) {
            FileUtil.mkdir(globalCodePathName);
        }

        // 用户代码隔离存放
        String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();
        String globalCodePathPath = userCodeParentPath + File.separator + GLOBAL_JAVA_CLASS_NAME;
        File userCodeFile = FileUtil.writeString(code, globalCodePathPath, StandardCharsets.UTF_8);

        String compileCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsolutePath());
        try {
            Process process = Runtime.getRuntime().exec(compileCmd);
            ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(process, "编译");
            System.out.println(executeMessage);
        } catch (IOException | InterruptedException e) {
            return getErrorResponse(e);
        }

        // 执行代码得到输出结果
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        int ifLongTime = 1;
        for (String inputArgs : inputList) {
            String runCmd = String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s;%s -Djava.security.manager=%s Main %s",
                    userCodeParentPath, SECURITY_MANAGER_PATH, SECURITY_MANAGER_CLASS_NAME, inputArgs);
            try {
                // 需要处理程序运行超时的问题
                Process process = Runtime.getRuntime().exec(runCmd);
                // 超时控制
                FutureTask<Integer> futureTask = new FutureTask<>(() -> {
                    long startTime = System.currentTimeMillis();
                    while (System.currentTimeMillis() - startTime < TIME_OUT) {
                        if (!process.isAlive()) {
                            // 结束了
                            return 1;
                        }
                    }
                    System.out.println("超时了，中断");
                    process.destroy();
                    return 0;
                });

                //ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(process, "运行");
                new Thread(futureTask).start();
                ExecuteMessage executeMessage = ProcessUtils.runInteractProcessAndGetMessage(process, "运行", inputArgs);
                Integer res = futureTask.get();
                if (res == 0) {
                    ifLongTime = 0;
                    break;
                }
                System.out.println(executeMessage);
                executeMessageList.add(executeMessage);
            } catch (IOException | InterruptedException | ExecutionException e) {
                return getErrorResponse(e);
            }
        }
        if (ifLongTime == 0) {
            executeCodeResponse.setStatus(3);
            executeCodeResponse.setMessage("代码执行时间过长");
            return executeCodeResponse;
        }
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
        judgeInfo.setMemory(0L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        executeCodeResponse.setOutputList(outputList);
        // 进行文件清理
        if (userCodeFile.getParentFile() != null) {
            boolean del = FileUtil.del(userCodeParentPath);
            System.out.println("删除" + (del ? "成功" : "失败"));
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
