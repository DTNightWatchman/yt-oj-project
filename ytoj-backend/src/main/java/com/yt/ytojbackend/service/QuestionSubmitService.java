package com.yt.ytojbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yt.ytojbackend.model.dto.question.QuestionQueryRequest;
import com.yt.ytojbackend.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.yt.ytojbackend.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.yt.ytojbackend.model.entity.Question;
import com.yt.ytojbackend.model.entity.QuestionSubmit;
import com.yt.ytojbackend.model.entity.User;
import com.yt.ytojbackend.model.vo.QuestionSubmitVO;
import com.yt.ytojbackend.model.vo.QuestionVO;
import com.yt.ytojbackend.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lenovo
 * @description 针对表【question_submit(题目提交表)】的数据库操作Service
 * @createDate 2023-08-08 00:11:41
 */
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return submitId
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /**
     * 获取帖子封装
     *
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页题目提交记录封装
     *
     * @param questionPage
     * @param loginUser
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionPage, User loginUser);

    /**
     * 分页获取我的提交记录
     * @param questionSubmitQueryRequest
     * @return
     */
    Page<QuestionSubmitVO> getMyQuestionSubmitByPage(QuestionSubmitQueryRequest questionSubmitQueryRequest, HttpServletRequest request);
}
