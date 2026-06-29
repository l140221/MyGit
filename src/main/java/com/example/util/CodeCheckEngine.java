package com.example.util;

import com.example.entity.CheckRule;
import com.example.entity.CodeIssue;
import com.example.enums.Severity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Java代码检测引擎
 * 对上传的源代码文件逐行扫描，匹配规则并生成问题报告
 */
public class CodeCheckEngine {

    /**
     * 对源代码执行全量检测
     * @param code       源代码内容
     * @param recordId   检测记录ID
     * @param rules      启用的规则列表
     * @return 检测出的问题列表
     */
    public List<CodeIssue> check(String code, Long recordId, List<CheckRule> rules) {
        List<CodeIssue> issues = new ArrayList<>();
        if (code == null || code.isEmpty()) return issues;

        String[] lines = code.split("\n", -1);

        for (CheckRule rule : rules) {
            switch (rule.getRuleName()) {
                case "ClassNamePascalCase":
                    checkClassNamePascalCase(code, lines, recordId, rule, issues);
                    break;
                case "MethodNameCamelCase":
                    checkMethodNameCamelCase(code, lines, recordId, rule, issues);
                    break;
                case "ConstantUpperSnake":
                    checkConstantUpperSnake(code, lines, recordId, rule, issues);
                    break;
                case "AvoidMagicNumber":
                    checkMagicNumber(lines, recordId, rule, issues);
                    break;
                case "MethodMaxLines":
                    checkMethodMaxLines(lines, recordId, rule, issues);
                    break;
                case "ClassMaxFields":
                    checkClassMaxFields(lines, recordId, rule, issues);
                    break;
                case "AvoidSystemExit":
                    checkPattern(code, lines, recordId, rule, issues,
                            Pattern.compile("System\\.exit"), Pattern.compile("//|\\*|\\*\\*"));
                    break;
                case "AvoidEval":
                    checkPattern(code, lines, recordId, rule, issues,
                            Pattern.compile("eval\\s*\\("), Pattern.compile("//|\\*|\\*\\*"));
                    break;
                case "AvoidEmptyCatch":
                    checkEmptyCatch(lines, recordId, rule, issues);
                    break;
                case "LineMaxLength":
                    checkLineMaxLength(lines, recordId, rule, issues);
                    break;
                case "AvoidPrintStackTrace":
                    checkPattern(code, lines, recordId, rule, issues,
                            Pattern.compile("\\.printStackTrace\\(\\)"), Pattern.compile("//|\\*|\\*\\*"));
                    break;
                default:
                    break;
            }
        }
        return issues;
    }

    private void checkClassNamePascalCase(String code, String[] lines, Long recordId,
                                           CheckRule rule, List<CodeIssue> issues) {
        Pattern classPattern = Pattern.compile("(public|private|protected)?\\s*(class|interface|enum)\\s+([a-zA-Z_][a-zA-Z0-9_]*)");
        var matcher = classPattern.matcher(code);
        while (matcher.find()) {
            String className = matcher.group(3);
            if (!Character.isUpperCase(className.charAt(0))) {
                int lineNum = getLineNumber(code, matcher.start());
                issues.add(new CodeIssue(recordId, lineNum, 1, Severity.MAJOR.name(),
                        rule.getRuleType(), rule.getRuleName(),
                        "类名 '" + className + "' 应使用大驼峰命名法（如 MyClass）",
                        getSnippet(lines, lineNum),
                        "建议修改为 " + Character.toUpperCase(className.charAt(0)) + className.substring(1)));
            }
        }
    }

    private void checkMethodNameCamelCase(String code, String[] lines, Long recordId,
                                           CheckRule rule, List<CodeIssue> issues) {
        Pattern methodPattern = Pattern.compile(
                "(public|private|protected|static|final|abstract|synchronized)\\s+" +
                "(\\w+(?:<\\w+>)?)\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*\\(");
        var matcher = methodPattern.matcher(code);
        while (matcher.find()) {
            String methodName = matcher.group(3);
            if (Character.isUpperCase(methodName.charAt(0)) &&
                !"main".equals(methodName) && !methodName.equals(lines[0].trim())) {
                int lineNum = getLineNumber(code, matcher.start());
                issues.add(new CodeIssue(recordId, lineNum, 1, Severity.MAJOR.name(),
                        rule.getRuleType(), rule.getRuleName(),
                        "方法名 '" + methodName + "' 应使用小驼峰命名法（如 myMethod）",
                        getSnippet(lines, lineNum),
                        "建议首字母小写"));
            }
        }
    }

    private void checkConstantUpperSnake(String code, String[] lines, Long recordId,
                                          CheckRule rule, List<CodeIssue> issues) {
        Pattern constPattern = Pattern.compile(
                "(public\\s+)?(static\\s+)?(final\\s+)(\\w+)\\s+([a-z][a-zA-Z0-9_]*)");
        var matcher = constPattern.matcher(code);
        while (matcher.find()) {
            if (matcher.group(2) != null && matcher.group(3) != null) {
                String constName = matcher.group(5);
                String type = matcher.group(4);
                if (!"Logger".equals(type) && !"log".equals(constName)) {
                    int lineNum = getLineNumber(code, matcher.start());
                    issues.add(new CodeIssue(recordId, lineNum, 1, Severity.MINOR.name(),
                            rule.getRuleType(), rule.getRuleName(),
                            "常量 '" + constName + "' 应使用大写蛇形命名法（如 MAX_VALUE）",
                            getSnippet(lines, lineNum),
                            "建议修改为 " + constName.toUpperCase()));
                }
            }
        }
    }

    private void checkMagicNumber(String[] lines, Long recordId, CheckRule rule, List<CodeIssue> issues) {
        Pattern magicNum = Pattern.compile("[^a-zA-Z0-9_](\\d{2,})[^a-zA-Z0-9_]");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.startsWith("//") || line.startsWith("*") || line.startsWith("import") ||
                line.contains("final") || line.contains("= ")) continue;
            var matcher = magicNum.matcher(line);
            if (matcher.find()) {
                issues.add(new CodeIssue(recordId, i + 1, 1, Severity.MINOR.name(),
                        rule.getRuleType(), rule.getRuleName(),
                        "发现魔法数字 '" + matcher.group(1) + "'，建议定义为常量",
                        line, "private static final int " + matcher.group(1) + "_VALUE = " + matcher.group(1) + ";"));
            }
        }
    }

    private void checkMethodMaxLines(String[] lines, Long recordId, CheckRule rule, List<CodeIssue> issues) {
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].matches(".*\\b(class|interface|enum)\\s+.*\\{")) {
                int start = i;
                int braceCount = 0;
                for (int j = i; j < lines.length; j++) {
                    braceCount += lines[j].chars().filter(c -> c == '{').count();
                    braceCount -= lines[j].chars().filter(c -> c == '}').count();
                    if (braceCount == 0 && j - start > 80) {
                        issues.add(new CodeIssue(recordId, start + 1, 1, Severity.MAJOR.name(),
                                rule.getRuleType(), rule.getRuleName(),
                                "类/方法从第 " + (start + 1) + " 行到第 " + (j + 1) + " 行共 "
                                + (j - start + 1) + " 行，超过80行限制",
                                lines[start] + " ... " + lines[j],
                                "建议拆分为多个小方法"));
                        break;
                    }
                    if (braceCount == 0) break;
                }
            }
        }
    }

    private void checkClassMaxFields(String[] lines, Long recordId, CheckRule rule, List<CodeIssue> issues) {
        int fieldCount = 0;
        int classStartLine = 0;
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.matches(".*\\bclass\\s+.*\\{")) {
                classStartLine = i + 1;
                fieldCount = 0;
            }
            if (line.matches("(private|protected|public)\\s+(static\\s+)?(final\\s+)?\\w+<?.+>?\\s+\\w+\\s*;")) {
                fieldCount++;
            }
            if (line.contains("}") && classStartLine > 0 && fieldCount > 20) {
                issues.add(new CodeIssue(recordId, classStartLine, 1, Severity.MAJOR.name(),
                        rule.getRuleType(), rule.getRuleName(),
                        "类共有 " + fieldCount + " 个字段，超过20个限制",
                        lines[classStartLine - 1],
                        "建议将部分字段提取到新类"));
                classStartLine = 0;
            }
        }
    }

    private void checkEmptyCatch(String[] lines, Long recordId, CheckRule rule, List<CodeIssue> issues) {
        for (int i = 0; i < lines.length - 1; i++) {
            if (lines[i].matches(".*\\bcatch\\s*\\(.*\\).*\\{")) {
                String nextLine = lines[i + 1].trim();
                if (nextLine.equals("}") || nextLine.startsWith("//") && lines[i + 2] != null && lines[i + 2].trim().equals("}")) {
                    issues.add(new CodeIssue(recordId, i + 1, 1, Severity.MAJOR.name(),
                            rule.getRuleType(), rule.getRuleName(),
                            "catch块不能为空",
                            lines[i] + "\n" + (i + 2 < lines.length ? lines[i + 1] : ""),
                            "请在catch中添加日志记录或异常处理"));
                }
            }
        }
    }

    private void checkLineMaxLength(String[] lines, Long recordId, CheckRule rule, List<CodeIssue> issues) {
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].length() > 120) {
                issues.add(new CodeIssue(recordId, i + 1, 1, Severity.MINOR.name(),
                        rule.getRuleType(), rule.getRuleName(),
                        "第 " + (i + 1) + " 行长度为 " + lines[i].length() + " 个字符，超过120字符限制",
                        lines[i].substring(0, Math.min(lines[i].length(), 120)) + "...",
                        "建议适当换行"));
            }
        }
    }

    private void checkPattern(String code, String[] lines, Long recordId, CheckRule rule,
                               List<CodeIssue> issues, Pattern pattern, Pattern commentPattern) {
        var matcher = pattern.matcher(code);
        while (matcher.find()) {
            int lineNum = getLineNumber(code, matcher.start());
            String line = lines[lineNum - 1].trim();
            if (!commentPattern.matcher(line).find()) {
                issues.add(new CodeIssue(recordId, lineNum, 1, rule.getSeverity(),
                        rule.getRuleType(), rule.getRuleName(),
                        rule.getDescription(),
                        line,
                        getSuggestionForPattern(rule.getRuleName())));
            }
        }
    }

    private String getSuggestionForPattern(String ruleName) {
        switch (ruleName) {
            case "AvoidSystemExit":
                return "建议使用抛出异常或返回错误码替代";
            case "AvoidEval":
                return "建议使用安全解析方案替代";
            case "AvoidPrintStackTrace":
                return "建议使用日志框架（如SLF4J）记录异常";
            default:
                return "";
        }
    }

    /**
     * 计算代码评分
     * 满分100，每个CRITICAL扣10分，MAJOR扣5分，MINOR扣2分，最低0分
     */
    public double calculateScore(int criticalCount, int majorCount, int minorCount) {
        double score = 100.0;
        score -= criticalCount * 10.0;
        score -= majorCount * 5.0;
        score -= minorCount * 2.0;
        return Math.max(0, score);
    }

    private int getLineNumber(String code, int position) {
        int line = 1;
        for (int i = 0; i < position && i < code.length(); i++) {
            if (code.charAt(i) == '\n') line++;
        }
        return line;
    }

    private String getSnippet(String[] lines, int lineNum) {
        if (lineNum <= 0 || lineNum > lines.length) return "";
        return lines[lineNum - 1].trim();
    }
}
