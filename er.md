# 个人博客系统 — 数据库实体关系图（ER图）

**版本：V1.0** · **日期：2026-05-11**

***

## 一、实体关系总览

```
                    ┌─────────┐
                    │  user   │
                    │ 用户实体 │
                    └────┬────┘
                         │
         ┌───────────────┼───────────────┐
         │ 1             │ M             │ M
         ▼               ▼               ▼
    ┌─────────┐    ╔═══════════╗    ╔════════════╗
    │ article │    ║  点赞关系  ║    ║  收藏关系   ║
    │ 文章实体 │    ║article_like║    ║article_    ║
    └────┬────┘    ╚═══════════╝    ║favorite     ║
         │                          ╚════════════╝
         │ 1
         ▼
    ┌──────────────┐
    │article_comment│  ← 独立实体
    │   评论实体    │
    └───┬──────────┘
        │ N
        ▼
    ┌──────┐
    │ user │  ← 评论也关联用户 (N:1)
    └──────┘
```

> 共 **3 个实体**（user、article、article\_comment）+ **2 个关联表**（article\_like、article\_favorite），对应数据库中 5 张表。

***

## 二、关系类型

| 关系                         |  类型 | 描述                         |
| -------------------------- | :-: | -------------------------- |
| user → article             | 1:N | 一个用户发布多篇文章                 |
| article → article\_comment | 1:N | 一篇文章有多条评论                  |
| user → article\_comment    | 1:N | 一个用户发表多条评论                 |
| user ↔ article（点赞）         | M:N | 通过 article\_like 关联表实现     |
| user ↔ article（收藏）         | M:N | 通过 article\_favorite 关联表实现 |

> 所有外键均为逻辑外键（应用层维护），数据库未设置 FOREIGN KEY 约束。

***

## 三、实体定义

### 3.1 实体 — user（用户表）

表名：`user` · 引擎：InnoDB

| 字段名          | 数据类型         | 约束条件                        | 说明              |
| ------------ | ------------ | --------------------------- | --------------- |
| id           | INT          | PRIMARY KEY AUTO\_INCREMENT | 用户唯一标识，自增主键     |
| user\_name   | VARCHAR(50)  | <br />                      | 用户显示名称          |
| password     | VARCHAR(100) | NOT NULL                    | 密码（BCrypt加密后存储） |
| email        | VARCHAR(100) | <br />                      | 电子邮箱，用于登录认证     |
| phone        | VARCHAR(20)  | <br />                      | 手机号码            |
| signature    | VARCHAR(255) | <br />                      | 个性签名文本          |
| avatar       | VARCHAR(255) | <br />                      | 头像图片URL路径       |
| create\_time | DATETIME     | <br />                      | 注册时间            |

***

### 3.2 实体 — article（文章表）

表名：`article` · 引擎：InnoDB

| 字段名              | 数据类型         | 约束条件                        | 说明                |
| ---------------- | ------------ | --------------------------- | ----------------- |
| id               | INT          | PRIMARY KEY AUTO\_INCREMENT | 文章唯一标识，自增主键       |
| article\_title   | VARCHAR(255) | <br />                      | 文章标题              |
| article\_sign    | VARCHAR(50)  | <br />                      | 文章标签（日常/技术/杂谈/其他） |
| author\_id       | INT          | FK → user.id                | 作者ID，外键关联user表    |
| author\_name     | VARCHAR(50)  | <br />                      | 作者名称（冗余存储）        |
| article\_content | TEXT         | <br />                      | 文章正文（Markdown格式）  |
| favorites        | INT          | DEFAULT 0                   | 收藏数量（应用层维护）       |
| likes            | INT          | DEFAULT 0                   | 点赞数量（应用层维护）       |
| create\_time     | DATETIME     | <br />                      | 创建时间              |
| change\_time     | DATETIME     | <br />                      | 最后修改时间            |

***

### 3.3 实体 — article\_comment（评论表）

表名：`article_comment` · 引擎：InnoDB · 字符集：utf8mb4

| 字段名          | 数据类型        | 约束条件                        | 说明          |
| ------------ | ----------- | --------------------------- | ----------- |
| id           | INT         | PRIMARY KEY AUTO\_INCREMENT | 评论唯一标识，自增主键 |
| article\_id  | INT         | NOT NULL，FK → article.id    | 外键，关联文章表    |
| user\_id     | INT         | NOT NULL，FK → user.id       | 外键，关联用户表    |
| user\_name   | VARCHAR(50) | <br />                      | 评论用户名（冗余存储） |
| content      | TEXT        | NOT NULL                    | 评论内容        |
| create\_time | DATETIME    | <br />                      | 创建时间        |
| change\_time | DATETIME    | <br />                      | 修改时间        |

索引：

- PRIMARY KEY (id)
- INDEX idx\_article\_id (article\_id)
- INDEX idx\_user\_id (user\_id)

***

### 3.4 关联表 — article\_like（点赞关联表）

表名：`article_like` · 引擎：InnoDB

| 字段名          | 数据类型     | 约束条件                        | 说明             |
| ------------ | -------- | --------------------------- | -------------- |
| id           | INT      | PRIMARY KEY AUTO\_INCREMENT | 唯一标识，自增主键      |
| article\_id  | INT      | FK → article.id             | 外键，关联文章表       |
| author\_id   | INT      | FK → user.id                | 外键，关联用户表（点赞用户） |
| create\_time | DATETIME | <br />                      | 点赞时间           |
| change\_time | DATETIME | <br />                      | 修改时间           |

> 业务约束：同一用户对同一文章不能重复点赞（应用层校验）。

***

### 3.5 关联表 — article\_favorite（收藏关联表）

表名：`article_favorite` · 引擎：InnoDB

| 字段名          | 数据类型     | 约束条件                        | 说明             |
| ------------ | -------- | --------------------------- | -------------- |
| id           | INT      | PRIMARY KEY AUTO\_INCREMENT | 唯一标识，自增主键      |
| article\_id  | INT      | FK → article.id             | 外键，关联文章表       |
| author\_id   | INT      | FK → user.id                | 外键，关联用户表（收藏用户） |
| create\_time | DATETIME | <br />                      | 收藏时间           |
| change\_time | DATETIME | <br />                      | 修改时间           |

> 业务约束：同一用户对同一文章不能重复收藏（应用层校验）。

***

## 四、外键关系汇总

| 子表/字段                         | 父表/字段      | 关系类型      | 删除策略   |
| ----------------------------- | ---------- | --------- | ------ |
| article.author\_id            | user.id    | 多对一 (N:1) | 不做级联删除 |
| article\_comment.article\_id  | article.id | 多对一 (N:1) | 不做级联删除 |
| article\_comment.user\_id     | user.id    | 多对一 (N:1) | 不做级联删除 |
| article\_like.article\_id     | article.id | 多对一 (N:1) | 不做级联删除 |
| article\_like.author\_id      | user.id    | 多对一 (N:1) | 不做级联删除 |
| article\_favorite.article\_id | article.id | 多对一 (N:1) | 不做级联删除 |
| article\_favorite.author\_id  | user.id    | 多对一 (N:1) | 不做级联删除 |

***

## 五、冗余字段说明

| 表名               | 冗余字段         | 设计目的                     |
| ---------------- | ------------ | ------------------------ |
| article          | author\_name | 避免查询文章时 JOIN user 表获取作者名 |
| article          | likes        | 避免每次展示文章时 COUNT 点赞表      |
| article          | favorites    | 避免每次展示文章时 COUNT 收藏表      |
| article\_comment | user\_name   | 避免查询评论时 JOIN user 表获取用户名 |

冗余字段维护规则：

- author\_name / user\_name：在创建记录时写入，用户改名后不追溯更新
- likes / favorites：在点赞/收藏/取消操作时由应用层代码同步 ±1

***

## 六、全文统计

| 表名                |  字段数量  |    约束条件    |     索引数量    |
| ----------------- | :----: | :--------: | :---------: |
| user              |    8   | 1 NOT NULL |    1 (PK)   |
| article           |   10   |      0     |    1 (PK)   |
| article\_comment  |    7   | 3 NOT NULL | 3 (PK+2IDX) |
| article\_like     |    5   |      0     |    1 (PK)   |
| article\_favorite |    5   |      0     |    1 (PK)   |
| **合计**            | **35** |    **4**   |    **7**    |

- 实体数：3
- 关联表数：2
- 关系数：5
- 主键数：5（均为 INT 自增）
- 外键字段数：6
- 冗余字段数：4

