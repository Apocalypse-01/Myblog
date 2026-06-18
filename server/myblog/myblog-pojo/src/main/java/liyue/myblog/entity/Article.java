package liyue.myblog.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    //文章唯一标识，id
    private String articleId;

    //文章名称
    private String articleTitle;

    //标签
    private String articleSign;

    //作者id
    private int authorId;

    //作者
    private String authorName;

    //文章内容
    private String articleContent;




}
