package com.yt.ytojbackend.judge.strategy;

import com.google.gson.Gson;
import com.yt.ytojbackend.model.dto.question.JudgeCase;
import com.yt.ytojbackend.model.dto.question.JudgeConfig;
import com.yt.ytojbackend.judge.codesandbox.model.JudgeInfo;
import com.yt.ytojbackend.model.entity.Question;
import com.yt.ytojbackend.model.enums.JudgeInfoMessageEnum;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Java 判题策略
 */
public class JavaLanguageJudgeStrategy implements JudgeStrategy{

    private final Gson gson = new Gson();

    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        // 根据代码沙箱的执行结果，设置题目的判题状态和题目信息

        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();
        Question question = judgeContext.getQuestion();

        // 判断执行返回的outputList和judgeCase中的outputList的结果是否是相同的
        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.WAITING;
        JudgeInfo judgeInfoResponse = new JudgeInfo();
        // 判断题目限制
        Long memory = judgeInfo.getMemory();
        Long time = judgeInfo.getTime();
        judgeInfoResponse.setMemory(memory);
        judgeInfoResponse.setTime(time);
        Boolean isEqual = judgeOutputList(inputList, outputList, judgeContext.getJudgeCaseList());
        if (!isEqual) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }

        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = gson.fromJson(judgeConfigStr, JudgeConfig.class);
        Long memoryLimit = judgeConfig.getMemoryLimit();
        Long timeLimit = judgeConfig.getTimeLimit();

        if (memory > memoryLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        Long JAVA_PROGRAM_TIME_COST = 10000L;
        if (time - JAVA_PROGRAM_TIME_COST > timeLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        // 结果是 accepted
        judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
        judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());

        return judgeInfoResponse;
    }

    /**
     * 判断结果是否符合预期
     * @param inputList
     * @param outputList
     * @param judgeCaseList
     * @return
     */
    private Boolean judgeOutputList(List<String> inputList, List<String> outputList, List<JudgeCase> judgeCaseList) {
        if (inputList.size() != outputList.size()) {
            return false;
        }
        List<String> outputListInCase = judgeCaseList.stream()
                .map(JudgeCase::getOutput).collect(Collectors.toList());
        if (outputListInCase.size() != outputList.size()) {
            return false;
        }

        for (int i = 0; i < outputList.size(); i++) {
            String s1 = outputList.get(i);
            String s2 = outputListInCase.get(i);
            if (!s1.equals(s2)) {
                return false;
            }
        }
        return true;
    }
}
