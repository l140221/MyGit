package com.example.dto;

import com.example.entity.CodeCheckRecord;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 检测结果DTO
 */
public class CheckResultDTO {
    private Long recordId;
    private String fileName;
    private String filePath;
    private String fileType;
    private String checkStatus;
    private Integer issueCount;
    private Integer criticalCount;
    private Integer majorCount;
    private Integer minorCount;
    private Double codeScore;
    private List<IssueDTO> issues;
    private LocalDateTime createTime;

    public static CheckResultDTO fromRecord(CodeCheckRecord record) {
        CheckResultDTO dto = new CheckResultDTO();
        dto.setRecordId(record.getId());
        dto.setFileName(record.getFileName());
        dto.setFilePath(record.getFilePath());
        dto.setFileType(record.getFileType());
        dto.setCheckStatus(record.getCheckStatus());
        dto.setIssueCount(record.getIssueCount());
        dto.setCriticalCount(record.getCriticalCount());
        dto.setMajorCount(record.getMajorCount());
        dto.setMinorCount(record.getMinorCount());
        dto.setCodeScore(record.getCodeScore());
        dto.setCreateTime(record.getCreateTime());
        return dto;
    }

    // getters & setters
    public Long getRecordId() { return recordId; }
    public void setRecordId(Long recordId) { this.recordId = recordId; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

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

    public List<IssueDTO> getIssues() { return issues; }
    public void setIssues(List<IssueDTO> issues) { this.issues = issues; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
