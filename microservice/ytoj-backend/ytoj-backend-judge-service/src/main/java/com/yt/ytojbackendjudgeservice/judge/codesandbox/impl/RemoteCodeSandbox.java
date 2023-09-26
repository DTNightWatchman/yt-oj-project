package com.yt.ytojbackendjudgeservice.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.util.StringUtils;
import com.yt.ytojbackendcommon.common.ErrorCode;
import com.yt.ytojbackendcommon.exception.BusinessException;
import com.yt.ytojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.yt.ytojbackendmodel.codesandbox.ExecuteCodeRequest;
import com.yt.ytojbackendmodel.codesandbox.ExecuteCodeResponse;

/**
 * 远程调用接口（实际调用接口的沙箱）
 */
public class RemoteCodeSandbox implements CodeSandbox {

    private static final String AUTH_REQUEST_HEADER = "auth";

    private static final String AUTH_REQUEST_SECRET = "secretkey";
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        String url = "http://localhost:9123/executeCode";
        String jsonStr = JSONUtil.toJsonStr(executeCodeRequest);
        String responseStr = HttpUtil.createPost(url).header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET)
                .body(jsonStr).execute().body();
        if (StringUtils.isBlank(responseStr)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "executeCode remoteSandbox error, message = " + responseStr);
        }
        return JSONUtil.toBean(responseStr, ExecuteCodeResponse.class);
    }
}
