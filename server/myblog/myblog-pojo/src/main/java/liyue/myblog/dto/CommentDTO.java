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
public class CommentDTO {

    private Integer id;

    private Integer articleId;

    private Integer userId;

    private String userName;

    private String content;

    private LocalDateTime createTime;

    private LocalDateTime changeTime;
}
