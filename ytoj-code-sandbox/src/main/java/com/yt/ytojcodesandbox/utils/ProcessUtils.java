package com.yt.ytojcodesandbox.utils;

import com.yt.ytojcodesandbox.model.ExecuteMessage;
import org.springframework.util.StopWatch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessUtils {


    public static ExecuteMessage runProcessAndGetMessage(Process process, String opName) throws IOException, InterruptedException {
        ExecuteMessage executeMessage = new ExecuteMessage();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // 等待程序执行拿到退出码
        int exitValue = process.waitFor();
        executeMessage.setExitValue(exitValue);
        if (exitValue == 0) {
            System.out.println(opName + "成功");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder compileOutputStringBuilder = new StringBuilder();
            String compileOutputLine = null;
            while ((compileOutputLine = bufferedReader.readLine()) != null) {
                compileOutputStringBuilder.append(compileOutputLine);
            }
            executeMessage.setMessage(compileOutputStringBuilder.toString());
        } else {
            System.out.println(opName + "失败,错误码:" + exitValue);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder compileOutputStringBuilder = new StringBuilder();
            String compileOutputLine = null;
            while ((compileOutputLine = bufferedReader.readLine()) != null) {
                compileOutputStringBuilder.append(compileOutputLine);
            }
            executeMessage.setMessage(compileOutputStringBuilder.toString());

            BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder errorCompileOutputStringBuilder = new StringBuilder();
            String errorCompileOutputLine = null;
            while ((errorCompileOutputLine = errorBufferedReader.readLine()) != null) {
                errorCompileOutputStringBuilder.append(errorCompileOutputLine);
            }
            executeMessage.setErrorMessage(errorCompileOutputStringBuilder.toString());
        }
        stopWatch.stop();
        executeMessage.setTime(stopWatch.getLastTaskTimeMillis());

        return executeMessage;
    }
}
