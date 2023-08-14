package com.yt.ytojbackend.model.dto.questionsubmit;

import lombok.Data;

/**
 * 题目配置
 */
@Data
public class JudgeInfo {

    /**
     * 程序执行信息
     */
    private String message;

    /**
     * 消耗内存
     */
    private Long memory;

    /**
     * 堆栈限制（KB）
     */
    private Long memoryLimit;
}
