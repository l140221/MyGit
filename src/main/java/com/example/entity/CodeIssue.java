package com.example.entity;

import java.time.LocalDateTime;

/**
 * 检测问题实体
 */
public class CodeIssue {
    private Long id;
    private Long recordId;
    private Integer line;
    private Integer column;
    private String severity;       // CRITICAL, MAJOR, MINOR
    private String type;           // NAMING, COMPLEXITY, STYLE, SECURITY, BUG
    private String ruleName;
    private String message;
    private String snippet;        // 问题代码片段
    private String suggestion;     // 修改建议
    private LocalDateTime createTime;

    public CodeIssue() {}

    public CodeIssue(Long recordId, Integer line, Integer column, String severity,
                     String type, String ruleName, String message, String snippet, String suggestion) {
        this.recordId = recordId;
        this.line = line;
        this.column = column;
        this.severity = severity;
        this.type = type;
        this.ruleName = ruleName;
        this.message = message;
        this.snippet = snippet;
        this.suggestion = suggestion;
        this.createTime = LocalDateTime.now();
    }

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRecordId() { return recordId; }
    public void setRecordId(Long recordId) { this.recordId = recordId; }

    public Integer getLine() { return line; }
    public void setLine(Integer line) { this.line = line; }

    public Integer getColumn() { return column; }
    public void setColumn(Integer column) { this.column = column; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getSnippet() { return snippet; }
    public void setSnippet(String snippet) { this.snippet = snippet; }

    public String getSuggestion() { return suggestion; }
    public void setSuggestion(String suggestion) { this.suggestion = suggestion; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
