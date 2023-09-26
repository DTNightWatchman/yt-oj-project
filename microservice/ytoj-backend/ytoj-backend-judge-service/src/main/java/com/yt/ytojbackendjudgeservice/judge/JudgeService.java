package com.yt.ytojbackendjudgeservice.judge;

import com.yt.ytojbackendmodel.model.vo.QuestionSubmitVO;

public interface JudgeService {

    QuestionSubmitVO doJudge(Long questionSubmitId);
}
