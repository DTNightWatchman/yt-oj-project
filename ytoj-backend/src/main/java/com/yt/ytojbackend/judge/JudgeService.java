package com.yt.ytojbackend.judge;

import com.yt.ytojbackend.judge.codesandbox.model.ExecuteCodeResponse;
import com.yt.ytojbackend.model.vo.QuestionSubmitVO;

public interface JudgeService {

    QuestionSubmitVO doJudge(Long questionSubmitId);
}
