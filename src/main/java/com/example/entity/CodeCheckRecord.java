package com.example.entity;

import java.time.LocalDateTime;

/**
 * 代码检测记录实体
 */
public class CodeCheckRecord {
    private Long id;
    private String fileName;
    private String filePath;
    private String fileType;
    private Long fileSize;
    private String checkStatus;      // PENDING, RUNNING, PASSED, FAILED
    private Integer issueCount;
    private Integer criticalCount;
    private Integer majorCount;
    private Integer minorCount;
    private Double codeScore;
    private String reportJson;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public CodeCheckRecord() {}

    public CodeCheckRecord(String fileName, String filePath, String fileType, Long fileSize) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.checkStatus = "PENDING";
        this.issueCount = 0;
        this.codeScore = 100.0;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public String getCheckStatus() { return checkStatus; }
    public void setCheckStatus(String checkStatus) { this.checkStatus = checkStatus; }

    public Integer getIssueCount() { return issueCount; }
    public void setIssueCount(Integer issueCount) { this.issueCount = issueCount; }

    public Integer getCriticalCount() { return criticalCount; }
    public void setCriticalCount(Integer criticalCount) { this.criticalCount = criticalCount; }

    public Integer getMajorCount() { return majorCount; }
    public void setMajorCount(Integer majorCount) { this.majorCount = majorCount; }

    public Integer getMinorCount() { return minorCount; }
    public void setMinorCount(Integer minorCount) { this.minorCount = minorCount; }

    public Double getCodeScore() { return codeScore; }
    public void setCodeScore(Double codeScore) { this.codeScore = codeScore; }

    public String getReportJson() { return reportJson; }
    public void setReportJson(String reportJson) { this.reportJson = reportJson; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
