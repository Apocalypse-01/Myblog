package liyue.myblog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticlePageDTO {


    //文章唯一标识，id
    private String id;

    //文章名称
    private String articleTitle;

    //标签
    private String articleSign;

    //作者
    private String authorName;

    //作者id
    private int authorId;

    //创建时间
    private LocalDateTime createTime;


}
