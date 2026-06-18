# MyBlog - 博客系统

基于 SpringBoot + Vue3 的个人知识管理博客系统，支持 Markdown 文章创作与编辑、EPUB 电子书下载、知识图谱可视化、AI 辅助创作、数据仪表盘等功能。

## 技术栈

### 后端
- **Spring Boot 3.3** - 后端框架
- **MyBatis-Plus** - ORM 框架
- **MySQL 8.0** - 数据库
- **JWT** - 身份认证
- **Knife4j / Springdoc** - API 文档

### 前端
- **Vue 3** - 前端框架
- **Vite 6** - 构建工具
- **Element Plus** - UI 组件库
- **Pinia** - 状态管理
- **ECharts** - 数据可视化
- **md-editor-v3** - Markdown 编辑器
- **Axios** - HTTP 请求

## 功能模块

| 模块 | 功能说明 |
|------|----------|
| 用户管理 | 注册、登录、个人信息修改、头像上传 |
| 文章管理 | Markdown 创作、编辑、删除、分页浏览 |
| 文章下载 | 支持 EPUB 格式电子书导出下载 |
| 知识图谱 | 基于 ECharts 的文章标签关系图谱可视化 |
| 数据仪表盘 | 文章统计、分类统计、趋势图表 |
| AI 辅助创作 | 接入 DeepSeek 大模型，辅助文章写作 |
| 点赞与收藏 | 文章点赞、收藏功能 |

## 项目结构

```
MyBlog/
├── client/vue-myblog/          # 前端项目
│   ├── src/
│   │   ├── api/                # 接口请求
│   │   ├── components/         # 页面组件
│   │   ├── router/             # 路由配置
│   │   ├── stores/             # 状态管理
│   │   └── utils/              # 工具函数
│   └── package.json
│
├── server/myblog/              # 后端项目
│   ├── myblog-pojo/            # 实体层（Entity/DTO/VO）
│   └── myblog-server/          # 服务层（Controller/Service/Mapper）
│       └── src/main/resources/
│           ├── mapper/          # MyBatis XML
│           └── application-example.properties
│
└── myblog.sql                  # 数据库初始化脚本
```

## 快速开始

### 环境要求
- JDK 17+
- Node.js 18+
- MySQL 8.0+
- Maven 3.8+

### 1. 初始化数据库

```bash
mysql -u root -p
CREATE DATABASE myblog CHARACTER SET utf8mb4;
USE myblog;
SOURCE myblog.sql;
```

### 2. 配置后端

复制配置模板并填写你的信息：

```bash
cp server/myblog/myblog-server/src/main/resources/application-example.properties \
   server/myblog/myblog-server/src/main/resources/application.properties
```

修改 `application.properties` 中的以下配置：

```properties
spring.datasource.username=你的数据库用户名
spring.datasource.password=你的数据库密码
ai.api.key=你的AI接口Key
```

### 3. 启动后端

```bash
cd server/myblog
mvn spring-boot:run
```

后端运行在 http://localhost:8081

### 4. 启动前端

```bash
cd client/vue-myblog
npm install
npm run dev
```

前端运行在 http://localhost:5173

## API 文档

启动后端后访问：http://localhost:8081/doc.html
