package com.example.util;

import com.example.entity.CodeCheckRecord;
import com.example.entity.CodeIssue;
import com.example.service.CodeCheckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 报告生成工具
 */
public class ReportGenerator {

    private static final Logger log = LoggerFactory.getLogger(ReportGenerator.class);
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 生成文本格式检测报告
     */
    public static String generateTextReport(CodeCheckRecord record, List<CodeIssue> issues) {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("  代码质量检测报告\n");
        sb.append("========================================\n\n");
        sb.append("文件名：").append(record.getFileName()).append("\n");
        sb.append("检测时间：").append(LocalDateTime.now().format(DTF)).append("\n");
        sb.append("文件大小：").append(record.getFileSize()).append(" bytes\n");
        sb.append("检测状态：").append(record.getCheckStatus()).append("\n\n");
        sb.append("--- 评分 ---\n");
        sb.append("代码评分：").append(String.format("%.1f", record.getCodeScore())).append(" / 100\n");
        sb.append("问题总数：").append(record.getIssueCount()).append("\n");
        sb.append("  · 致命：").append(record.getCriticalCount()).append("\n");
        sb.append("  · 严重：").append(record.getMajorCount()).append("\n");
        sb.append("  · 一般：").append(record.getMinorCount()).append("\n\n");

        if (issues != null && !issues.isEmpty()) {
            sb.append("--- 问题详情 ---\n");
            for (int i = 0; i < issues.size(); i++) {
                CodeIssue issue = issues.get(i);
                sb.append("\n").append(i + 1).append(". [").append(issue.getSeverity()).append("] ");
                sb.append("第").append(issue.getLine()).append("行 · ").append(issue.getMessage()).append("\n");
                sb.append("   代码片段：").append(issue.getSnippet()).append("\n");
                sb.append("   修改建议：").append(issue.getSuggestion()).append("\n");
            }
        } else {
            sb.append("\n🎉 未发现代码质量问题！\n");
        }

        sb.append("\n========================================\n");
        sb.append("  【代码质量检测系统】 自动生成\n");
        sb.append("========================================\n");
        return sb.toString();
    }
}
