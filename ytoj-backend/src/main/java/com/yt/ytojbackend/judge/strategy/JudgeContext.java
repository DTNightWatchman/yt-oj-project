package com.yt.ytojbackend.judge.strategy;

import com.yt.ytojbackend.model.dto.question.JudgeCase;
import com.yt.ytojbackend.judge.codesandbox.model.JudgeInfo;
import com.yt.ytojbackend.model.entity.Question;
import com.yt.ytojbackend.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * 判题上下文
 */
@Data
public class JudgeContext {

    /**
     * 判题信息
     */
    private JudgeInfo judgeInfo;

    /**
     * 输入用例
     */
    private List<String> inputList;

    /**
     * 输入用例
     */
    private List<String> outputList;

    /**
     *
     */
    private List<JudgeCase> judgeCaseList;

    /**
     * 题目信息
     */
    private Question question;

    /**
     * 题目提交信息
     */
    private QuestionSubmit questionSubmit;
}
