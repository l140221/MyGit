package com.example.entity;

import java.time.LocalDateTime;

/**
 * 检测规则实体
 */
public class CheckRule {
    private Long id;
    private String ruleName;
    private String ruleType;          // NAMING, COMPLEXITY, STYLE, SECURITY, BUG
    private String severity;          // CRITICAL, MAJOR, MINOR
    private String description;
    private String ruleContent;       // 规则表达式或匹配模式
    private Boolean enabled;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public CheckRule() {}

    public CheckRule(String ruleName, String ruleType, String severity, String description) {
        this.ruleName = ruleName;
        this.ruleType = ruleType;
        this.severity = severity;
        this.description = description;
        this.enabled = true;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }

    public String getRuleType() { return ruleType; }
    public void setRuleType(String ruleType) { this.ruleType = ruleType; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getRuleContent() { return ruleContent; }
    public void setRuleContent(String ruleContent) { this.ruleContent = ruleContent; }

    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
