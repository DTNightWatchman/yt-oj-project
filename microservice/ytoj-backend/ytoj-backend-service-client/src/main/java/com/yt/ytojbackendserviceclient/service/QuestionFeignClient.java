package com.yt.ytojbackendserviceclient.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yt.ytojbackendmodel.model.dto.question.QuestionQueryRequest;
import com.yt.ytojbackendmodel.model.entity.Question;
import com.yt.ytojbackendmodel.model.entity.QuestionSubmit;
import com.yt.ytojbackendmodel.model.vo.QuestionVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
* @author lenovo
* @description 针对表【question(题目表)】的数据库操作Service
* @createDate 2023-08-08 00:11:03
*/
@FeignClient(name = "ytoj-backend-question-service", path = "/api/question/inner")
public interface QuestionFeignClient {

    /**
     * 获取题目
     * @param questionId
     * @return
     */
    @GetMapping("/get")
    Question getQuestionById(@RequestParam("questionId") long questionId);

    /**
     * 获取提交信息
     * @param questionSubmitId
     * @return
     */
    @GetMapping("/get/question_submit")
    QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") long questionSubmitId);


    /**
     * 获取帖子封装
     *
     * @param questionSubmit
     * @return
     */
    @PostMapping("question_submit/update")
    boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit);


}
