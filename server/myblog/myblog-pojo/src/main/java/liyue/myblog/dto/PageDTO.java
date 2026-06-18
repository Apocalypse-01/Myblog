package liyue.myblog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO {

    //总数量，总条数
    private int total;

    //单个页面条数
    private int pageSize;

    //当前页面
    private int currentPage;

    //列表数量
    List<ArticlePageDTO> articlePageDTOList;

}
