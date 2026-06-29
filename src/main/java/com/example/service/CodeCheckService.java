package com.example.service;

import com.example.dto.CheckResultDTO;
import com.example.dto.StatsOverviewDTO;
import com.example.entity.CodeCheckRecord;
import com.example.entity.CodeIssue;

import java.util.List;

/**
 * 代码质量检测服务接口
 */
public interface CodeCheckService {

    /**
     * 执行代码检测
     * @param content  源代码内容
     * @param fileName 文件名
     * @param fileType 文件类型
     * @return 检测结果
     */
    CheckResultDTO checkCode(String content, String fileName, String fileType);

    /**
     * 根据ID获取检测结果
     */
    CheckResultDTO getResultById(Long id);

    /**
     * 获取所有检测记录
     */
    List<CheckResultDTO> getAllRecords();

    /**
     * 根据记录ID获取具体问题列表
     */
    List<CodeIssue> getIssuesByRecordId(Long recordId);

    /**
     * 获取统计概览
     */
    StatsOverviewDTO getStatsOverview();
}
