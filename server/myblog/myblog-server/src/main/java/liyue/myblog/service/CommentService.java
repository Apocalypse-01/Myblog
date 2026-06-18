package liyue.myblog.service;

import liyue.myblog.dto.CommentDTO;
import liyue.myblog.vo.CommentVO;

import java.util.List;

public interface CommentService {

    void addComment(CommentVO commentVO);

    List<CommentDTO> getCommentsByArticleId(Integer articleId);

    void deleteComment(Integer id, Integer userId);

    int getCommentCountByArticleId(Integer articleId);
}
