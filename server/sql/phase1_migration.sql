-- 第一阶段功能补全 - 数据库变更脚本

-- 1. 创建评论表（如果不存在）
CREATE TABLE IF NOT EXISTS article_comment (
    id INT PRIMARY KEY AUTO_INCREMENT,
    article_id INT NOT NULL,
    user_id INT NOT NULL,
    user_name VARCHAR(50),
    content TEXT NOT NULL,
    create_time DATETIME,
    change_time DATETIME,
    INDEX idx_article_id(article_id),
    INDEX idx_user_id(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. 扩展password字段长度以支持BCrypt加密（BCrypt密码约60字符）
ALTER TABLE user MODIFY COLUMN password VARCHAR(100) NOT NULL;

-- 3. 为文章表添加全文索引（如果不存在）
-- ALTER TABLE article ADD FULLTEXT INDEX ft_search(article_title, article_content);
