package com.example.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 系统启动时运行的初始化任务（含示例检测演示）
 */
@Component
public class SampleDataRunner implements CommandLineRunner {

    private final com.example.service.CodeCheckService codeCheckService;

    public SampleDataRunner(com.example.service.CodeCheckService codeCheckService) {
        this.codeCheckService = codeCheckService;
    }

    @Override
    public void run(String... args) {
        // 预先执行一次示例检测，作为演示数据
        String sampleCode = """
                package com.example.demo;

                import java.util.Scanner;

                public class sampleClass {
                    private int value = 42;
                    private String name = "test";

                    public void doSomething(String input) {
                        int x = 100;
                        System.out.println("hello");
                        System.exit(0);
                    }

                    static final int MaxCount = 50;

                    public static void main(String[] args) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                    }
                }
                """;

        try {
            codeCheckService.checkCode(sampleCode, "SampleDemo.java", ".java");
        } catch (Exception e) {
            // 启动时示例检测失败不影响主流程
        }
    }
}
