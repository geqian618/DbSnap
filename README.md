<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">TableStructureGenerator</h1>

![SpringBoot 2.4.2](https://img.shields.io/badge/SpringBoot-2.4.2-blue)
![Vue 2](https://img.shields.io/badge/VUE-2-blue)
![element ui](https://img.shields.io/badge/ElementUI--blue)
![MySQL 8](https://img.shields.io/badge/MySQL-8-blue)
![Oracle 11g](https://img.shields.io/badge/Oracle-11g-blue)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL--blue)
![Sql Server](https://img.shields.io/badge/SqlServer-2017-blue)
![DB2](https://img.shields.io/badge/DB2--blue)
![MariaDB](https://img.shields.io/badge/MariaDB--blue)
![Clickhouse](https://img.shields.io/badge/Clickhouse--blue)
![达梦](https://img.shields.io/badge/达梦-8-blue)
![TIDB](https://img.shields.io/badge/TIDB--blue)
![SQLite 3](https://img.shields.io/badge/SQLite-3-blue)
![Derby](https://img.shields.io/badge/Derby--blue)
![H2](https://img.shields.io/badge/H2--blue)

## 简介
一个简单易用且高效的数据库表结构文档生成工具，让编写数据库表结构文档变得简单
<br>

## 开发缘由

在工作中编写开发文档时，通常会涉及到数据库表结构这个部分。最开始使用各类文档编辑软件新建表格，然后逐个复制和粘贴内容，屏幕来回切换到头昏眼花，CV操作到手抽筋，不注意还可能看错数据或填写表格时填错数据。之前也有尝试过编码+修改配置文件的方式，但是在更改数据库连接信息、导出哪些表及列信息时需要手动修改配置文件，使用起来并不是很方便。于是就萌生了开发该项目的想法，以简化数据库文档的编写。
<br>
<br>

## 技术栈

### 后端

- 采用 SpringBoot 作为基础框架
- 采用 apache poi 生成word文档
- 采用 itextpdf 生成pdf文档
- 采用 snakeyaml 解析yaml文件

### 前端

- 采用 Vue2 作为基础框架
- 采用 Element ui 作为组件库
  <br>
  <br>

## 优势

- 灵活性高，可根据需求灵活选择导出的表列数据
- 扩展性良好，适配新的数据库类型，只需提供对应的查询SQL语句，几乎不需要额外的编码
- 树形控件采用懒加载模式加载数据，避免数据量太大时，一次性加载全部数据，造成严重卡顿
- 多线程加载数据，提高数据量较大时的导出速度

<br>

## 支持数据库类型

- MySQL
- Oracle
- PostgreSQL
- Sql Server
- DB2
- MariaDB
- Clickhouse
- 达梦
- TIDB
- Derby（内嵌式）
- H2（内嵌式）
- SQLite3（内嵌式）

<br>


## 支持文档类型
  
- Word
- PDF
- Markdown
- HTML

<br>

## 项目演示

**1. 创建数据库连接**
![image-20230719021014570](src/main/resources/static/images/创建数据库连接.png)
<br>
<br>

**2. 连接数据库**
![image-20230719021014570](src/main/resources/static/images/连接数据库.png)
<br>
<br>

**3. 文档预览**
![image-20230719021014570](src/main/resources/static/images/文档预览.png)
<br>
<br>

**4. 选择导出列名**
![image-20230719013433699](src/main/resources/static/images/选择列名.png)  
<br>
<br>

**5. 下载pdf文档（示例）**

![image-20230719013337074](src/main/resources/static/images/pdf文档.png)
<br>
<br>

**6. 下载word文档（示例）**

![image-20230719013403179](src/main/resources/static/images/word文档.png)  
<br>
<br>

**7. 下载markdown文档（示例）**

![image-20230719013403179](src/main/resources/static/images/markdown文档.png)  
<br>
<br>

**8. 下载HTML文档（示例）**

![image-20230719013403179](src/main/resources/static/images/html文档.png)  
<br>
<br>


## windows下载
```shell
链接：https://pan.baidu.com/s/1PKHjclAsCJ5PnsV7JHRloA 
提取码：w3xc
```
<br>
<br>

## 使用说明

**默认访问地址：http://localhost:8888/**
> 项目启成功后自动打开浏览器进行访问

<br>
<br>


## Docker安装
<br>
<br>


## 赞赏
<img src="src/main/resources/static/images/appreciation-code.jpg" alt="赞赏码" height="270">

<br>
<br>

## 常见问题
**① tableStructureGenerator.exe 启动失败，可能原因**
- 端口冲突：tableStructureGenerator.exe 所在目录下创建文件 tableStructureGenerator.exe.vmoptions, 添加如下配置并更改端口号
  ```shell
  -Dserver.port=8888
  ```
- 默认使用 win64 位操作系统环境，不兼容 win32 位操作系统环境（后续考虑兼容）


## 参考说明

数据库连接页面布局参考至：https://gitee.com/pomz/database-export