package liyue.myblog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO {

    private Integer id;

    private Integer authorId;

    //作者
    private String authorName;

    //文章名称
    private String articleTitle;

    //标签
    private String articleSign;

    //文章内容
    private String articleContent;

    //收藏数
    private Integer favorites;

    //点赞数
    private Integer likes;

    //创建时间
    private LocalDateTime createTime;

    //修改时间
    private LocalDateTime changeTime;


}
