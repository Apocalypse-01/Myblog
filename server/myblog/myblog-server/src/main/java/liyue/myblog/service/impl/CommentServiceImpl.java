package liyue.myblog.service.impl;

import liyue.myblog.dto.CommentDTO;
import liyue.myblog.mapper.CommentMapper;
import liyue.myblog.service.CommentService;
import liyue.myblog.vo.CommentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentMapper commentMapper;

    @Override
    public void addComment(CommentVO commentVO) {
        CommentDTO commentDTO = new CommentDTO();
        BeanUtils.copyProperties(commentVO, commentDTO);
        commentDTO.setCreateTime(LocalDateTime.now());
        commentDTO.setChangeTime(LocalDateTime.now());
        log.info("添加评论: {}", commentDTO);
        commentMapper.insertComment(commentDTO);
    }

    @Override
    public List<CommentDTO> getCommentsByArticleId(Integer articleId) {
        log.info("获取文章评论, articleId: {}", articleId);
        return commentMapper.getCommentsByArticleId(articleId);
    }

    @Override
    public void deleteComment(Integer id, Integer userId) {
        log.info("删除评论, id: {}, userId: {}", id, userId);
        commentMapper.deleteComment(id, userId);
    }

    @Override
    public int getCommentCountByArticleId(Integer articleId) {
        return commentMapper.getCommentCountByArticleId(articleId);
    }
}
