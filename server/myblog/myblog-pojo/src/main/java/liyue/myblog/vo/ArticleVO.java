package liyue.myblog.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleVO {

    private Integer id;

    private String articleTitle;

    //标签
    private String articleSign;

    //文章内容
    private String articleContent;

    //作者id
    private int authorId;

    //作者
    private String authorName;

    //创建时间
    private LocalDateTime createTime;

    //修改时间
    private LocalDateTime changeTime;


}
