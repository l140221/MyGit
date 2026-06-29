package com.example.mapper;

import com.example.entity.SystemConfig;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 系统配置内存存储
 */
public class SystemConfigMapper {
    private final ConcurrentHashMap<String, SystemConfig> store = new ConcurrentHashMap<>();

    public SystemConfigMapper() {
        SystemConfig cfg = new SystemConfig();
        cfg.setConfigKey("threshold.score.pass");
        cfg.setConfigValue("60");
        cfg.setDescription("检测通过分数阈值");
        store.put("threshold.score.pass", cfg);
    }

    public String getValue(String key) {
        SystemConfig config = store.get(key);
        return config == null ? null : config.getConfigValue();
    }

    public void setValue(String key, String value) {
        SystemConfig config = store.get(key);
        if (config == null) {
            config = new SystemConfig();
            config.setConfigKey(key);
        }
        config.setConfigValue(value);
        store.put(key, config);
    }
}
