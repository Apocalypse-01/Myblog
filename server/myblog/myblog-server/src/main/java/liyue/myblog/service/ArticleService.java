package liyue.myblog.service;


import liyue.myblog.dto.ArticleCommentDTO;
import liyue.myblog.dto.ArticleDTO;
import liyue.myblog.dto.ArticlePageDTO;
import liyue.myblog.dto.DashboardDTO;
import liyue.myblog.dto.KnowledgeGraphDTO;
import liyue.myblog.dto.PageDTO;
import liyue.myblog.vo.ArticleVO;

import java.util.List;

public interface ArticleService {


    /**
     * 获取图像信息，根据articleId
     * @param articleId
     * @return
     */
    ArticleDTO getByArticleId(String articleId);

    /**
     * 插入文章，添加文章，返回文章ID
     */
    int insertArticle(ArticleVO articleVO);

    /**
     * 分页查询，获取文章列表
     * @param authorId
     * @return
     */
    PageDTO getPageByArticleId(Integer authorId,Integer currentPage);

    /**
     * 分页查询，获取全部文章列表，根据 标签 和 收藏数/点赞数
     * @param articleSign
     * @param currentPage
     * @param order
     * @return
     */
    PageDTO getAllPage(Integer articleSign, Integer currentPage, Integer order);

    /**
     * 获取 文章信息
     * @return
     */
    ArticleDTO getArticleByArticleId(Integer articleId);

    /**
     * 获取 收藏夹文章信息
     * @param authorId
     * @return
     */
    PageDTO getFavoritesByAuthorId(Integer authorId,Integer currentPage);

    /**
     * 获取 点赞的 文章信息
     * @param authorId
     * @return
     */
    PageDTO getLikesByAuthorId(Integer authorId, Integer currentPage);

    /**
     * 点赞，将文章id加入到点赞表当中
     */
    int  insertArticleLikes(Integer articleId, Integer authorId, String authorName);

    /**
     * 取消点赞，从 点赞表当 中 删除该文章
     * @param articleId
     * @param authorId
     * @param authorName
     * @return
     */
    int deleteArticleLikes(Integer articleId, Integer authorId, String authorName);

    /**
     * 收藏，将文章id加入到 收藏夹 当中
     */
    int insertArticleFavorites(Integer articleId, Integer authorId, String authorName);

    /**
     * 取消收藏，从 收藏表当 中 删除该文章
     * @param articleId
     * @param authorId
     * @param authorName
     * @return
     */
    int deleteArticleFavorites(Integer articleId, Integer authorId, String authorName);

    /**
     * 通过文章id，获取文章评论
     * @param articleId
     * @return
     */
    List<ArticleCommentDTO> getArticleComment(Integer articleId);

    PageDTO searchArticle(String keyword, Integer currentPage);

    void updateArticle(ArticleVO articleVO);

    void deleteArticle(Integer id, Integer authorId);

    List<ArticlePageDTO> getAllByAuthorId(Integer authorId);

    KnowledgeGraphDTO getKnowledgeGraph(Integer authorId);

    DashboardDTO getDashboard(Integer authorId);
}
