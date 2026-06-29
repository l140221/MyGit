package com.example.enums;

/**
 * 检测严重级别枚举
 */
public enum Severity {
    CRITICAL("致命", 10),
    MAJOR("严重", 5),
    MINOR("一般", 2);

    private final String label;
    private final int scoreDeduction;

    Severity(String label, int scoreDeduction) {
        this.label = label;
        this.scoreDeduction = scoreDeduction;
    }

    public String getLabel() { return label; }
    public int getScoreDeduction() { return scoreDeduction; }
}
