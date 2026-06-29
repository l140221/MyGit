package com.example.service.impl;

import com.example.dto.CheckResultDTO;
import com.example.dto.IssueDTO;
import com.example.dto.StatsOverviewDTO;
import com.example.entity.CodeCheckRecord;
import com.example.entity.CodeIssue;
import com.example.entity.CheckRule;
import com.example.mapper.CheckRuleMapper;
import com.example.mapper.CodeCheckRecordMapper;
import com.example.mapper.CodeIssueMapper;
import com.example.service.CodeCheckService;
import com.example.util.CodeCheckEngine;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 代码质量检测服务实现
 */
@Service
public class CodeCheckServiceImpl implements CodeCheckService {

    private final CodeCheckRecordMapper recordMapper = new CodeCheckRecordMapper();
    private final CodeIssueMapper issueMapper = new CodeIssueMapper();
    private final CheckRuleMapper ruleMapper = new CheckRuleMapper();
    private final CodeCheckEngine engine = new CodeCheckEngine();

    @Override
    public CheckResultDTO checkCode(String content, String fileName, String fileType) {
        // 1. 创建检测记录
        CodeCheckRecord record = new CodeCheckRecord(fileName, "/" + fileName, fileType,
                (long) content.length());
        record.setCheckStatus("RUNNING");
        Long recordId = recordMapper.insert(record);

        // 2. 获取启用的规则
        List<CheckRule> rules = ruleMapper.selectEnabled();

        // 3. 执行检测
        List<CodeIssue> allIssues = engine.check(content, recordId, rules);

        // 4. 统计问题
        int criticalCount = 0, majorCount = 0, minorCount = 0;
        for (CodeIssue issue : allIssues) {
            switch (issue.getSeverity()) {
                case "CRITICAL": criticalCount++; break;
                case "MAJOR": majorCount++; break;
                case "MINOR": minorCount++; break;
            }
        }

        // 5. 计算评分
        double score = engine.calculateScore(criticalCount, majorCount, minorCount);

        // 6. 更新记录
        record.setCheckStatus(allIssues.isEmpty() ? "PASSED" : "FAILED");
        record.setIssueCount(allIssues.size());
        record.setCriticalCount(criticalCount);
        record.setMajorCount(majorCount);
        record.setMinorCount(minorCount);
        record.setCodeScore(score);
        recordMapper.update(record);

        // 7. 保存问题
        if (!allIssues.isEmpty()) {
            issueMapper.batchInsert(allIssues);
        }

        // 8. 返回结果
        CheckResultDTO result = CheckResultDTO.fromRecord(record);
        result.setIssues(allIssues.stream().map(this::toIssueDTO).collect(Collectors.toList()));
        return result;
    }

    @Override
    public CheckResultDTO getResultById(Long id) {
        CodeCheckRecord record = recordMapper.selectById(id);
        if (record == null) return null;
        CheckResultDTO result = CheckResultDTO.fromRecord(record);
        List<CodeIssue> issues = issueMapper.selectByRecordId(id);
        result.setIssues(issues.stream().map(this::toIssueDTO).collect(Collectors.toList()));
        return result;
    }

    @Override
    public List<CheckResultDTO> getAllRecords() {
        return recordMapper.selectAll().stream()
                .map(CheckResultDTO::fromRecord)
                .collect(Collectors.toList());
    }

    @Override
    public List<CodeIssue> getIssuesByRecordId(Long recordId) {
        return issueMapper.selectByRecordId(recordId);
    }

    @Override
    public StatsOverviewDTO getStatsOverview() {
        StatsOverviewDTO stats = new StatsOverviewDTO();
        stats.setTotalFiles(recordMapper.count());
        stats.setTotalChecks(recordMapper.count());
        stats.setPassedFiles(recordMapper.countByStatus("PASSED"));
        stats.setFailedFiles(recordMapper.countByStatus("FAILED"));
        stats.setTotalIssues(recordMapper.sumIssues());
        stats.setAverageScore(recordMapper.avgScore());
        return stats;
    }

    private IssueDTO toIssueDTO(CodeIssue issue) {
        IssueDTO dto = new IssueDTO();
        dto.setId(issue.getId());
        dto.setLine(issue.getLine());
        dto.setColumn(issue.getColumn());
        dto.setSeverity(issue.getSeverity());
        dto.setType(issue.getType());
        dto.setRuleName(issue.getRuleName());
        dto.setMessage(issue.getMessage());
        dto.setSnippet(issue.getSnippet());
        dto.setSuggestion(issue.getSuggestion());
        return dto;
    }
}
