package com.yt.ytojbackendquestionservice.controller.inner;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yt.ytojbackendmodel.model.entity.Question;
import com.yt.ytojbackendmodel.model.entity.QuestionSubmit;
import com.yt.ytojbackendquestionservice.service.QuestionService;
import com.yt.ytojbackendquestionservice.service.QuestionSubmitService;
import com.yt.ytojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: YT
 * @Description: 题目模块内部调用
 * @DateTime: 2023/9/26$ - 10:54
 */

@RestController
@RequestMapping("/inner")
public class QuestionInnerController implements QuestionFeignClient {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    /**
     * 获取题目
     * @param questionId
     * @return
     */
    @Override
    @GetMapping("/get")
    public Question getQuestionById(long questionId) {
        return questionService.getById(questionId);
    }

    /**
     * 获取提交信息
     * @param questionSubmitId
     * @return
     */
    @Override
    @GetMapping("/get/question_submit")
    public QuestionSubmit getQuestionSubmitById(long questionSubmitId) {
        return questionSubmitService.getById(questionSubmitId);
    }

    /**
     * 获取帖子封装
     *
     * @param questionSubmit
     * @return
     */
    @Override
    @PostMapping("question_submit/update")
    public boolean updateQuestionSubmitById(QuestionSubmit questionSubmit) {
        return questionSubmitService.updateById(questionSubmit);
    }

    @Override
    @GetMapping("/question_submit/updateSubmitNumAndAcceptedNum")
    public boolean updateSubmitNumAndAcceptedNum(long questionId, int ifAccepted) {
        UpdateWrapper<Question> updateWrapper = new UpdateWrapper<>();
        updateWrapper.setSql("submitNum = submitNum + 1");
        updateWrapper.setSql(ifAccepted == 1, "acceptedNum = acceptedNum + 1");
        updateWrapper.lambda().eq(Question::getId, questionId);
        return questionService.update(updateWrapper);
    }
}
