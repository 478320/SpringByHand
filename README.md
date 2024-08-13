# 手写Spring全家桶——从源码入门



## 简介 

手写Spring全家桶框架是一个包含

1. **手写Spring包扫描，@Autowire，AOP，Spring事务**

2. **SpringMVC仿源码编写，拦截器，异常处理器**

3. **Springboot内置tomcat整合手写SpringMVC启动**

的整合项目，旨在让不了解Spring的同学更深入的理解Spring工作原理



## 功能介绍

该项目模拟了 Spring全家桶 最主要的功能，功能较多详情查看帮助文档

帮助文档链接...(emmm还没写)

## 项目亮点和测试说明

https://lwaqkk4m1y9.feishu.cn/wiki/DkOOwJ0PFi7jc7kfvL6crjDhnyf?from=from_copylink

## 待改进点

Spring部分不够完善，仍需时间补充

## 环境要求

JDK8+

JDK版本在8以上会因为Cglib代理不兼容无法使用可以使用jvm启动参数

```
--add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.lang.reflect=ALL-UNNAMED
```

暂时解决

## 快速开始

下面仓库为手写Spring全家桶使用示例参考

https://github.com/478320/SpringByHandQuickStart.git

## 编者的话

目前项目尚不完善，仅可用于学习使用