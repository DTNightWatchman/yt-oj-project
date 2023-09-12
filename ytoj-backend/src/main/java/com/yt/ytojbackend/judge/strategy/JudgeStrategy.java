package com.yt.ytojbackend.judge.strategy;

import com.yt.ytojbackend.judge.codesandbox.model.JudgeInfo;

public interface JudgeStrategy {

    JudgeInfo doJudge(JudgeContext judgeContext);
}
