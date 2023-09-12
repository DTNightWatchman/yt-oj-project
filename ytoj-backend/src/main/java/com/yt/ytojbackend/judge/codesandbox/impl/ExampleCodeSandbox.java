package com.yt.ytojbackend.judge.codesandbox.impl;
import com.yt.ytojbackend.judge.codesandbox.model.JudgeInfo;

import com.yt.ytojbackend.judge.codesandbox.CodeSandbox;
import com.yt.ytojbackend.judge.codesandbox.model.ExecuteCodeRequest;
import com.yt.ytojbackend.judge.codesandbox.model.ExecuteCodeResponse;
import com.yt.ytojbackend.model.enums.JudgeInfoMessageEnum;
import com.yt.ytojbackend.model.enums.QuestionSubmitStatusEnum;

/**
 * 示例调用接口（仅为跑通业务流程）
 */
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(executeCodeRequest.getInputList());
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);

        return executeCodeResponse;
    }
}
