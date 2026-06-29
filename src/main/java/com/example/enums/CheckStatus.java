package com.example.enums;

/**
 * 检测状态枚举
 */
public enum CheckStatus {
    PENDING("待检测"),
    RUNNING("检测中"),
    PASSED("通过"),
    FAILED("未通过");

    private final String label;
    CheckStatus(String label) { this.label = label; }
    public String getLabel() { return label; }
}
