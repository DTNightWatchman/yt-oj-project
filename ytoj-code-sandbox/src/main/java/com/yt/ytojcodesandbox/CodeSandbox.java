package com.yt.ytojcodesandbox;

import com.yt.ytojcodesandbox.model.ExecuteCodeRequest;
import com.yt.ytojcodesandbox.model.ExecuteCodeResponse;

public interface CodeSandbox {

    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
