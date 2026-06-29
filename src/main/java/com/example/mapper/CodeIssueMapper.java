package com.example.mapper;

import com.example.entity.CodeIssue;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 检测问题内存存储
 */
public class CodeIssueMapper {
    private final ConcurrentHashMap<Long, List<CodeIssue>> store = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);

    public void batchInsert(List<CodeIssue> issues) {
        for (CodeIssue issue : issues) {
            issue.setId(idGen.getAndIncrement());
            store.computeIfAbsent(issue.getRecordId(), k -> new CopyOnWriteArrayList<>()).add(issue);
        }
    }

    public List<CodeIssue> selectByRecordId(Long recordId) {
        return store.getOrDefault(recordId, new ArrayList<>());
    }

    public void deleteByRecordId(Long recordId) {
        store.remove(recordId);
    }

    public long countByRecordId(Long recordId) {
        List<CodeIssue> issues = store.get(recordId);
        return issues == null ? 0 : issues.size();
    }
}
