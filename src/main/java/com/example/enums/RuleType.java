package com.example.enums;

/**
 * 规则类型枚举
 */
public enum RuleType {
    NAMING("命名规范"),
    COMPLEXITY("复杂度"),
    STYLE("代码风格"),
    SECURITY("安全检测"),
    BUG("潜在缺陷");

    private final String label;
    RuleType(String label) { this.label = label; }
    public String getLabel() { return label; }
}
