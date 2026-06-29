package com.example.dto;

/**
 * 代码上传与检测请求DTO
 */
public class CodeCheckRequest {
    private String content;      // 源代码内容
    private String fileName;     // 文件名
    private String fileType;     // 文件类型

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
}
