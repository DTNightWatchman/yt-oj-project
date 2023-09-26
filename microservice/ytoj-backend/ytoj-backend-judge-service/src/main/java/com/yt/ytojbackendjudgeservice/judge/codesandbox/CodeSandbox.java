package com.yt.ytojbackendjudgeservice.judge.codesandbox;

import com.yt.ytojbackendmodel.codesandbox.ExecuteCodeRequest;
import com.yt.ytojbackendmodel.codesandbox.ExecuteCodeResponse;

/**
 * 代码沙箱
 */
public interface CodeSandbox {

    /**
     * 代码沙箱实现接口
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
