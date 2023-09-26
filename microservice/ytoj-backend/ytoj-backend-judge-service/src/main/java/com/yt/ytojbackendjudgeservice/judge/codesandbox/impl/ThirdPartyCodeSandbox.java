package com.yt.ytojbackendjudgeservice.judge.codesandbox.impl;

import com.yt.ytojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.yt.ytojbackendmodel.codesandbox.ExecuteCodeRequest;
import com.yt.ytojbackendmodel.codesandbox.ExecuteCodeResponse;

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
