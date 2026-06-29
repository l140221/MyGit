package com.example.controller;

import com.example.dto.*;
import com.example.entity.CodeIssue;
import com.example.service.CodeCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 代码质量检测 - REST API 控制器
 */
@RestController
@RequestMapping("/api/code-check")
public class CodeCheckController {

    @Autowired
    private CodeCheckService codeCheckService;

    /**
     * 提交源代码进行质量检测
     * POST /api/code-check/check
     */
    @PostMapping("/check")
    public ApiResult<CheckResultDTO> checkCode(@RequestBody CodeCheckRequest request) {
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            return ApiResult.error(400, "源代码内容不能为空");
        }
        if (request.getFileName() == null) {
            request.setFileName("Unknown.java");
        }
        CheckResultDTO result = codeCheckService.checkCode(
                request.getContent(),
                request.getFileName(),
                request.getFileType() != null ? request.getFileType() : ".java"
        );
        return ApiResult.success("检测完成", result);
    }

    /**
     * 获取检测结果详情
     * GET /api/code-check/result/{id}
     */
    @GetMapping("/result/{id}")
    public ApiResult<CheckResultDTO> getResult(@PathVariable Long id) {
        CheckResultDTO result = codeCheckService.getResultById(id);
        if (result == null) {
            return ApiResult.error(404, "检测记录不存在");
        }
        return ApiResult.success(result);
    }

    /**
     * 获取所有检测记录列表
     * GET /api/code-check/records
     */
    @GetMapping("/records")
    public ApiResult<List<CheckResultDTO>> getAllRecords() {
        List<CheckResultDTO> records = codeCheckService.getAllRecords();
        return ApiResult.success(records);
    }

    /**
     * 获取检测结果中的问题列表
     * GET /api/code-check/issues/{recordId}
     */
    @GetMapping("/issues/{recordId}")
    public ApiResult<List<IssueDTO>> getIssues(@PathVariable Long recordId) {
        List<CodeIssue> issues = codeCheckService.getIssuesByRecordId(recordId);
        List<IssueDTO> issueDTOs = issues.stream()
                .map(issue -> {
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
                })
                .collect(Collectors.toList());
        return ApiResult.success(issueDTOs);
    }

    /**
     * 获取系统统计概览
     * GET /api/code-check/stats
     */
    @GetMapping("/stats")
    public ApiResult<StatsOverviewDTO> getStats() {
        StatsOverviewDTO stats = codeCheckService.getStatsOverview();
        return ApiResult.success(stats);
    }

    /**
     * 健康检查
     * GET /api/code-check/health
     */
    @GetMapping("/health")
    public ApiResult<String> health() {
        return ApiResult.success("代码质量检测系统运行正常");
    }
}
