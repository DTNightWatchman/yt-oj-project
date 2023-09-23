package com.yt.ytojbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yt.ytojbackend.common.BaseResponse;
import com.yt.ytojbackend.common.ErrorCode;
import com.yt.ytojbackend.common.ResultUtils;
import com.yt.ytojbackend.exception.BusinessException;
import com.yt.ytojbackend.model.dto.postthumb.PostThumbAddRequest;
import com.yt.ytojbackend.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.yt.ytojbackend.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.yt.ytojbackend.model.entity.Question;
import com.yt.ytojbackend.model.entity.QuestionSubmit;
import com.yt.ytojbackend.model.entity.User;
import com.yt.ytojbackend.model.vo.QuestionSubmitVO;
import com.yt.ytojbackend.model.vo.UserVO;
import com.yt.ytojbackend.service.PostThumbService;
import com.yt.ytojbackend.service.QuestionSubmitService;
import com.yt.ytojbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目提交接口
 *
 * @author <a href="https://github.com/DTNightWatchman">YTbaiduren</a>
 * @from
 */
//@RestController
//@RequestMapping("/question_submit")
//@Slf4j
//@Deprecated
public class QuestionSubmitController {

//    @Resource
//    private QuestionSubmitService questionSubmitService;
//
//    @Resource
//    private UserService userService;
//
//    /**
//     * 提交题目
//     *
//     * @param questionSubmitAddRequest
//     * @param request
//     * @return
//     */
//    @PostMapping("/")
//    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
//            HttpServletRequest request) {
//        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        // 登录才能点赞
//        final User loginUser = userService.getLoginUser(request);
//        long questionId = questionSubmitAddRequest.getQuestionId();
//        long result = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
//        return ResultUtils.success(result);
//    }
//    /**
//     * 分页获取题目提交列表（除了管理员外，普通用户）
//     * @return
//     */
//    @PostMapping("/list/page")
//    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest,
//                                                                         HttpServletRequest request) {
//        long current = questionSubmitQueryRequest.getCurrent();
//        long size = questionSubmitQueryRequest.getPageSize();
//        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size),
//                questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
//        User loginUser = userService.getLoginUser(request);
//        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage, loginUser));
//    }

}
