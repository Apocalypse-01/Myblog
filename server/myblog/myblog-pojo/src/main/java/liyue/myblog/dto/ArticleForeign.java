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
public class ArticleForeign {

    //文章唯一标识，id
    private Integer articleId;

    //作者id
    private int authorId;

    //作者
    private String authorName;

    //创建时间
    private LocalDateTime createTime;

    //修改时间
    private LocalDateTime changeTime;




}
