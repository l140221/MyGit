package com.example.constant;

/**
 * 系统常量
 */
public interface CodeCheckConstants {

    /** 代码评分满分 */
    double MAX_SCORE = 100.0;

    /** 评分阈值：优秀 */
    double EXCELLENT_THRESHOLD = 90.0;

    /** 评分阈值：良好 */
    double GOOD_THRESHOLD = 75.0;

    /** 评分阈值：及格 */
    double PASS_THRESHOLD = 60.0;

    /** 单文件最大检测行数 */
    int MAX_LINES_PER_FILE = 5000;

    /** 最大检测文件数 */
    int MAX_FILES_PER_BATCH = 100;

    /** 检测超时时间（秒） */
    int CHECK_TIMEOUT_SECONDS = 300;

    /** 默认分页大小 */
    int DEFAULT_PAGE_SIZE = 20;
}
