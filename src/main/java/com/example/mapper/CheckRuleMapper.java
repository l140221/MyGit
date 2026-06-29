package com.example.mapper;

import com.example.entity.CheckRule;
import com.example.enums.RuleType;
import com.example.enums.Severity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 检测规则内存存储（含预置规则）
 */
public class CheckRuleMapper {
    private final ConcurrentHashMap<Long, CheckRule> store = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);

    public CheckRuleMapper() {
        initDefaultRules();
    }

    private void initDefaultRules() {
        addRule(new CheckRule("ClassNamePascalCase", RuleType.NAMING.name(), Severity.MAJOR.name(),
                "类名应使用大驼峰命名法（PascalCase）"));
        addRule(new CheckRule("MethodNameCamelCase", RuleType.NAMING.name(), Severity.MAJOR.name(),
                "方法名应使用小驼峰命名法（camelCase）"));
        addRule(new CheckRule("ConstantUpperSnake", RuleType.NAMING.name(), Severity.MINOR.name(),
                "常量名应使用大写蛇形命名法（UPPER_SNAKE_CASE）"));
        addRule(new CheckRule("AvoidMagicNumber", RuleType.STYLE.name(), Severity.MINOR.name(),
                "避免使用魔法数字，应定义为常量"));
        addRule(new CheckRule("MethodMaxLines", RuleType.COMPLEXITY.name(), Severity.MAJOR.name(),
                "方法体不应超过80行"));
        addRule(new CheckRule("ClassMaxFields", RuleType.COMPLEXITY.name(), Severity.MAJOR.name(),
                "类中字段数不应超过20个"));
        addRule(new CheckRule("AvoidSystemExit", RuleType.SECURITY.name(), Severity.CRITICAL.name(),
                "禁止直接调用System.exit()"));
        addRule(new CheckRule("AvoidEval", RuleType.SECURITY.name(), Severity.CRITICAL.name(),
                "禁止使用eval()执行动态代码"));
        addRule(new CheckRule("AvoidNullCheck", RuleType.BUG.name(), Severity.MAJOR.name(),
                "避免空指针，应进行null检查"));
        addRule(new CheckRule("AvoidEmptyCatch", RuleType.BUG.name(), Severity.MAJOR.name(),
                "catch块不应为空"));
        addRule(new CheckRule("LineMaxLength", RuleType.STYLE.name(), Severity.MINOR.name(),
                "单行代码不应超过120个字符"));
        addRule(new CheckRule("AvoidPrintStackTrace", RuleType.STYLE.name(), Severity.MINOR.name(),
                "应使用日志框架替代printStackTrace()"));
    }

    private void addRule(CheckRule rule) {
        Long id = idGen.getAndIncrement();
        rule.setId(id);
        store.put(id, rule);
    }

    public List<CheckRule> selectAll() {
        return new ArrayList<>(store.values());
    }

    public List<CheckRule> selectEnabled() {
        List<CheckRule> result = new ArrayList<>();
        for (CheckRule r : store.values()) {
            if (r.getEnabled()) result.add(r);
        }
        return result;
    }

    public CheckRule selectById(Long id) {
        return store.get(id);
    }

    public void update(CheckRule rule) {
        store.put(rule.getId(), rule);
    }
}
