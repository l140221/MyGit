package com.example.mapper;

import com.example.entity.CodeCheckRecord;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 检测记录内存存储（模拟数据库操作）
 */
public class CodeCheckRecordMapper {
    private final ConcurrentHashMap<Long, CodeCheckRecord> store = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);

    public Long insert(CodeCheckRecord record) {
        Long id = idGen.getAndIncrement();
        record.setId(id);
        store.put(id, record);
        return id;
    }

    public int update(CodeCheckRecord record) {
        store.put(record.getId(), record);
        return 1;
    }

    public CodeCheckRecord selectById(Long id) {
        return store.get(id);
    }

    public List<CodeCheckRecord> selectAll() {
        return new ArrayList<>(store.values());
    }

    public List<CodeCheckRecord> selectByStatus(String status) {
        List<CodeCheckRecord> result = new ArrayList<>();
        for (CodeCheckRecord r : store.values()) {
            if (r.getCheckStatus().equals(status)) {
                result.add(r);
            }
        }
        return result;
    }

    public long count() {
        return store.size();
    }

    public long countByStatus(String status) {
        return store.values().stream()
                .filter(r -> r.getCheckStatus().equals(status))
                .count();
    }

    public long sumIssues() {
        return store.values().stream()
                .mapToLong(CodeCheckRecord::getIssueCount)
                .sum();
    }

    public double avgScore() {
        return store.values().stream()
                .mapToDouble(CodeCheckRecord::getCodeScore)
                .average()
                .orElse(0.0);
    }
}
