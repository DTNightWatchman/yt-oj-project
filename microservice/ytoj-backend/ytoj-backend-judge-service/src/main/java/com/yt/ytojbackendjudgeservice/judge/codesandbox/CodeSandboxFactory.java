package com.yt.ytojbackendjudgeservice.judge.codesandbox;

import com.yt.ytojbackendjudgeservice.judge.codesandbox.impl.ExampleCodeSandbox;
import com.yt.ytojbackendjudgeservice.judge.codesandbox.impl.RemoteCodeSandbox;
import com.yt.ytojbackendjudgeservice.judge.codesandbox.impl.ThirdPartyCodeSandbox;

/**
 * 代码沙箱工厂（根据字符串参数创建指定的代码沙箱实例）
 */
public class CodeSandboxFactory {

    public static CodeSandbox newInstance(String type) {
        switch (type) {
            case "example":
                return new CodeSandboxProxy(new ExampleCodeSandbox());
            case "remote":
                return new CodeSandboxProxy(new RemoteCodeSandbox());
            case "thirdParty":
                return new CodeSandboxProxy(new ThirdPartyCodeSandbox());
            default:
                return new CodeSandboxProxy(new ExampleCodeSandbox());
        }
    }
}
