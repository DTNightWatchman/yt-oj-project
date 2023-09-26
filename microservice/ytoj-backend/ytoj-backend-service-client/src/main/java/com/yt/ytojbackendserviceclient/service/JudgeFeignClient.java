package com.yt.ytojbackendserviceclient.service;


import com.yt.ytojbackendmodel.model.vo.QuestionSubmitVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 调用判题服务接口
 */
@FeignClient(name = "ytoj-backend-judge-service", path = "/api/judge/inner")
public interface JudgeFeignClient {

    @PostMapping("/do")
    QuestionSubmitVO doJudge(@RequestParam("questionSubmitId") Long questionSubmitId);
}
