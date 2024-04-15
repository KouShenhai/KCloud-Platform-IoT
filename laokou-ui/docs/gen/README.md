代码生成
====



## 需求

> 因代码生成由后端实现，所以若需要使用代码生成功能来生成前端代码的话，需要以下修改。



## 实现方案

1. 将本目录下`antdv`文件夹放入`RuoYi-Vue`中代码生成模块(ruoyi-generator)中`resource`的`vm`文件夹下；

2. 将本目录中`VelocityUtils.java`文件替换掉代码生成模块中`com.ruoyi.generator.util`的`VelocityUtils.java`文件；

3. 重启后台项目，代码生成即由`element-ui`成功改为`ant design vue`。

   

![需替换及新增的位置](https://oss.fuzui.net/img/20210202004013.png)


