package com.yt.ytojcodesandbox.utils;

import com.yt.ytojcodesandbox.model.ExecuteMessage;
import org.apache.catalina.LifecycleState;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StopWatch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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

            List<String> outputStrList = new ArrayList<>();
            String compileOutputLine = null;
            while ((compileOutputLine = bufferedReader.readLine()) != null) {
                outputStrList.add(compileOutputLine);
            }
            executeMessage.setMessage(StringUtils.join(outputStrList, "\n"));
        } else {
            System.out.println(opName + "失败,错误码:" + exitValue);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            List<String> compileOutputStrList = new ArrayList<>();

            String compileOutputLine = null;
            while ((compileOutputLine = bufferedReader.readLine()) != null) {
                compileOutputStrList.add(compileOutputLine);
            }
            executeMessage.setMessage(StringUtils.join(compileOutputStrList, "\n"));

            // 异常结果
            BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String errorCompileOutputLine = null;
            List<String> errorOutputStrList = new ArrayList<>();

            while ((errorCompileOutputLine = errorBufferedReader.readLine()) != null) {
                errorOutputStrList.add(errorCompileOutputLine);
            }
            executeMessage.setErrorMessage(StringUtils.join(errorOutputStrList, "\n"));
        }
        stopWatch.stop();
        executeMessage.setTime(stopWatch.getLastTaskTimeMillis());

        return executeMessage;
    }
}
