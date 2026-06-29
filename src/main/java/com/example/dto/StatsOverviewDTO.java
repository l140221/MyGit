package com.example.dto;

/**
 * 统计概览DTO
 */
public class StatsOverviewDTO {
    private long totalFiles;
    private long totalChecks;
    private long passedFiles;
    private long failedFiles;
    private long totalIssues;
    private double averageScore;

    public long getTotalFiles() { return totalFiles; }
    public void setTotalFiles(long totalFiles) { this.totalFiles = totalFiles; }

    public long getTotalChecks() { return totalChecks; }
    public void setTotalChecks(long totalChecks) { this.totalChecks = totalChecks; }

    public long getPassedFiles() { return passedFiles; }
    public void setPassedFiles(long passedFiles) { this.passedFiles = passedFiles; }

    public long getFailedFiles() { return failedFiles; }
    public void setFailedFiles(long failedFiles) { this.failedFiles = failedFiles; }

    public long getTotalIssues() { return totalIssues; }
    public void setTotalIssues(long totalIssues) { this.totalIssues = totalIssues; }

    public double getAverageScore() { return averageScore; }
    public void setAverageScore(double averageScore) { this.averageScore = averageScore; }
}
