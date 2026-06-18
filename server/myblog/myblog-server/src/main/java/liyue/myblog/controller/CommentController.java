package liyue.myblog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import liyue.myblog.dto.CommentDTO;
import liyue.myblog.result.Result;
import liyue.myblog.service.CommentService;
import liyue.myblog.vo.CommentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@Tag(name = "评论模块")
@Slf4j
public class CommentController {

    @Autowired
    CommentService commentService;

    @PostMapping("/add")
    @Operation(summary = "添加评论")
    public Result addComment(@RequestBody CommentVO commentVO) {
        log.info("添加评论: {}", commentVO);
        commentService.addComment(commentVO);
        return Result.success();
    }

    @GetMapping("/list")
    @Operation(summary = "获取文章评论列表")
    public Result<List<CommentDTO>> getCommentList(@RequestParam("articleId") Integer articleId) {
        log.info("获取文章评论, articleId: {}", articleId);
        List<CommentDTO> commentList = commentService.getCommentsByArticleId(articleId);
        return Result.success(commentList);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除评论")
    public Result deleteComment(@RequestParam("id") Integer id, @RequestParam("userId") Integer userId) {
        log.info("删除评论, id: {}, userId: {}", id, userId);
        commentService.deleteComment(id, userId);
        return Result.success();
    }

    @GetMapping("/count")
    @Operation(summary = "获取评论数量")
    public Result<Integer> getCommentCount(@RequestParam("articleId") Integer articleId) {
        int count = commentService.getCommentCountByArticleId(articleId);
        return Result.success(count);
    }
}
