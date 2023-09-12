package com.yt.ytojbackend.judge.codesandbox;

import com.yt.ytojbackend.judge.codesandbox.model.ExecuteCodeRequest;
import com.yt.ytojbackend.judge.codesandbox.model.ExecuteCodeResponse;

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
