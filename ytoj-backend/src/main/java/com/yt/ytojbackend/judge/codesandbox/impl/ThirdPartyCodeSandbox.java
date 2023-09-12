package com.yt.ytojbackend.judge.codesandbox.impl;

import com.yt.ytojbackend.judge.codesandbox.CodeSandbox;
import com.yt.ytojbackend.judge.codesandbox.model.ExecuteCodeRequest;
import com.yt.ytojbackend.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * 第三方代码沙箱（调用网上线程的第三方代码沙箱）
 */
public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
