package com.xcy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class SpringSecurity05AuthApplication {

    public static void main(String[] args) {
        // 启动 Spring 应用：也就是将应用跑起来，不管是web项目、还是正常的程序应用都是可以的
        // 分析源码：就是从 SpringApplication.run(); 开始分析
        // -- 项目运行最开始就是执行 run() 方法然后开始加载运行项目代码
        // -- 具体 run() 方法中具体执行的大步骤可以划分为以下几大部。
        // 1、获取并启动监听器z
        // 2、根据 SpringApplicationRunListeners 准备运行环境
        // 3、Banner 打印器：进行打印 Banner 图标
        // 4、启动 Spring 容器
        // 5、Spring 容器前置处理
        // 6、刷新容器 --> this.refreshContext(context); 主要的地方，扫描包、配置bean、初始化项目等等，SpringBoot的自动配置
        // 7、打出结束前处理时间
        // 8、执行 Runners 返回容器
        SpringApplication.run(SpringSecurity05AuthApplication.class, args);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();


    }

}
