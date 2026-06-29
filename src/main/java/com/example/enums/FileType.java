package com.example.enums;

/**
 * 文件类型枚举
 */
public enum FileType {
    JAVA(".java"),
    XML(".xml"),
    PROPERTIES(".properties"),
    HTML(".html"),
    JS(".js"),
    CSS(".css"),
    YAML(".yml", ".yaml"),
    SQL(".sql");

    private final String[] extensions;
    FileType(String... extensions) { this.extensions = extensions; }
    public String[] getExtensions() { return extensions; }

    public static FileType fromExtension(String ext) {
        for (FileType ft : values()) {
            for (String e : ft.extensions) {
                if (e.equalsIgnoreCase(ext)) return ft;
            }
        }
        return null;
    }
}
