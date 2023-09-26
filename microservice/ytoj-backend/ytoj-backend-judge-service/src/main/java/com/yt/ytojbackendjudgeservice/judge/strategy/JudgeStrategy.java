package com.yt.ytojbackendjudgeservice.judge.strategy;

import com.yt.ytojbackendmodel.codesandbox.JudgeInfo;

public interface JudgeStrategy {

    JudgeInfo doJudge(JudgeContext judgeContext);
}
