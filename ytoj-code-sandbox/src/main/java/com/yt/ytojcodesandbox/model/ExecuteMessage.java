package com.yt.ytojcodesandbox.model;

import lombok.Data;

/**
 * 进程执行信息
 */
@Data
public class ExecuteMessage {

    private Integer exitValue;

    private String message;

    /**
     * 错误信息
     */
    private String errorMessage;


    private Long time;

}
