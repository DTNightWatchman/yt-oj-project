package com.yt.ytojbackendjudgeservice.controller.inner;

import com.yt.ytojbackendjudgeservice.judge.JudgeService;
import com.yt.ytojbackendmodel.model.vo.QuestionSubmitVO;
import com.yt.ytojbackendserviceclient.service.JudgeFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: YT
 * @Description: 判题服务内部调用
 * @DateTime: 2023/9/26$ - 11:06
 */
@RestController
@RequestMapping("/inner")
public class JudgeInnerController implements JudgeFeignClient {

    @Resource
    private JudgeService judgeService;

    /**
     * 判题服务调用
     * @param questionSubmitId
     * @return
     */
    @Override
    @PostMapping("/do")
    public QuestionSubmitVO doJudge(@RequestParam("questionSubmitId") Long questionSubmitId) {
        return judgeService.doJudge(questionSubmitId);
    }
}
