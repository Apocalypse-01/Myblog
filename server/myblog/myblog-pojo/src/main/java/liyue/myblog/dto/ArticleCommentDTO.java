package liyue.myblog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCommentDTO {

    //文章唯一标识，id
    private Integer articleId;

    //作者id
    private Integer authorId;

    //作者
    private String authorName;

    //文章内容
    private String authorComment;



}
