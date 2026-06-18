package liyue.myblog.mapper;

import liyue.myblog.dto.ArticleCommentDTO;
import liyue.myblog.dto.ArticleDTO;
import liyue.myblog.dto.ArticleForeign;
import liyue.myblog.dto.ArticlePageDTO;
import liyue.myblog.dto.DashboardDTO;
import liyue.myblog.entity.Article;
import liyue.myblog.vo.ArticleVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {

    /**
     * 获取信息，根据articleId
     * @param articleId
     * @return
     */
    @Select("select * from article where id = #{articleId}")
    ArticleDTO getByArticleId(String articleId);

    /**
     * 添加文章
     * @param articleDTO
     */
    void insertArticle(ArticleDTO articleDTO);

    /**
     * 分页查询，获取文章列表
     * @param authorId
     * @return
     */
    List<ArticlePageDTO> getPageByauthorId(int authorId,int start,int end);

    /**
     * 根据id 获得列表总数量
     * @param authorId
     * @return
     */
    int getTotalByauthorId(int authorId);

    /**
     * 根据标签，获取文章列表
     * @param sign
     * @param start
     * @param num
     * @param orderC
     * @return
     */
    List<ArticlePageDTO> getPageBySign(String sign, int start, int num, String orderC);

    /**
     * 根据时间，获取文章列表
     * @param start
     * @param num
     * @param orderC
     * @return
     */
    List<ArticlePageDTO> getPageByTime(int start, int num, String orderC);

    List<ArticlePageDTO> getPageByAllOrder(int start, int num, String orderC);

    /**
     * 根据标签，获取列表总数量
     * @param sign
     * @return
     */
    int getTotalBySign(String sign);

    @Select("select count(id) from article")
    int getTotalAll();

    /**
     * 获取文章详细信息
     * @param articleId
     * @return
     */
    ArticleDTO getArticle(Integer articleId);

    /**
     * 根据 authorId 获取收藏文章列表
     * @param authorId
     * @return
     */
    List<ArticlePageDTO> getArticleFavorites(@Param("authorId") Integer authorId, @Param("start") int start, @Param("num") int num);

    /**
     * 根据 authorId 获取 收藏文章数量， 收藏夹当中
     * @param authorId
     * @return
     */
    int getTotalInAF(Integer authorId);

    /**
     * 根据 authorId  获取 点赞文章列表
     * @param authorId
     * @return
     */
    List<ArticlePageDTO> getArticleLikes(@Param("authorId") Integer authorId, @Param("start") int start, @Param("num") int num);

    /**
     * 根据 authorId 获取 点赞文章数量， 点赞表当中
     * @param authorId
     * @return
     */
    int getTotalInAL(Integer authorId);

    /**
     *点赞，将文章id加入到点赞表当中
     * @param articleLike
     */
    void insertArticleLike(ArticleForeign articleLike);

    /**
     * 将文章对应的 点赞数 增加一
     * @param articleId
     */
    @Update("update article set likes = likes + 1 where id = #{articleId};")
    void addArticleLikes(Integer articleId);

    /**
     * 查询点赞数 根据articleId
     */
    @Select("select likes from article where id = #{articleId}")
    int getTotalLikesByArticleId(Integer articleId);

    /**
     * 删除 点赞的文章
     * @param articleId
     * @param authorId
     */
    @Delete("delete from article_like where article_id = #{articleId} and author_id = #{authorId}")
    void deleteArticleLike(Integer articleId, Integer authorId);
    /**
     * 将文章对应的 点赞数 减少一
     * @param articleId
     */
    @Update("update article set likes = likes - 1 where id = #{articleId};")
    void subArticleLikes(Integer articleId);

    /**
     * 查询是否已经点赞
     * @param articleId
     * @param authorId
     * @return
     */
    @Select("select * from article_like where author_id= #{authorId} and article_id = #{articleId};")
    List<ArticleForeign> getIsLike(Integer articleId, Integer authorId);

    /**
     * 查询是否已经收藏
     * @param articleId
     * @param authorId
     * @return
     */
    @Select("select id from article_favorite where author_id= #{authorId} and article_id = #{articleId};")
    List<ArticleForeign> getIsFavorites(Integer articleId, Integer authorId);

    /**
     * 文章 添加到 收藏夹 当中
     * @param articleFavorites
     */
    void insertArticleFavorites(ArticleForeign articleFavorites);

    /**
     * 将文章对应的 收藏数 增加一
     * @param articleId
     */
    @Update("update article set favorites = favorites + 1 where id = #{articleId};")
    void addArticleFavorites(Integer articleId);

    /**
     * 查询收藏数 根据articleId
     * @param articleId
     * @return
     */
    @Select("select favorites from article where id = #{articleId}")
    int getTotalFavoritesByArticleId(Integer articleId);

    /**
     * 删除文章，从收藏夹当中
     * @param articleId
     * @param authorId
     */
    @Delete("delete from article_favorite where article_id = #{articleId} and author_id = #{authorId}")
    void deleteArticleFavorites(Integer articleId, Integer authorId);

    /**
     * 将对应文章的 收藏数 减一
     * @param articleId
     */
    @Update("update article set favorites = favorites - 1 where id = #{articleId};")
    void subArticleFavorites(Integer articleId);

    /**
     *
     * @param articleId
     * @return
     */
    @Select("select user_id and create_time and comments from  article_comment where article_id=#{articleId} order by create_time asc")
    List<ArticleCommentDTO> getArticleComments(Integer articleId);

    @Select("select id as articleId, article_title, article_sign, author_id, author_name, article_content, favorites, likes, create_time from article where article_title like CONCAT('%',#{keyword},'%') or article_content like CONCAT('%',#{keyword},'%') order by create_time desc limit #{start}, #{pageSize}")
    List<ArticlePageDTO> searchByKeyword(@Param("keyword") String keyword, @Param("start") int start, @Param("pageSize") int pageSize);

    @Select("select count(id) from article where article_title like CONCAT('%',#{keyword},'%') or article_content like CONCAT('%',#{keyword},'%')")
    int getSearchTotal(@Param("keyword") String keyword);

    void updateArticle(ArticleDTO articleDTO);

    void deleteArticle(@Param("id") Integer id, @Param("authorId") Integer authorId);

    @Select("select id, article_title, article_sign, author_id, author_name, create_time from article where author_id = #{authorId} order by create_time desc")
    List<ArticlePageDTO> getAllByAuthorId(@Param("authorId") int authorId);

    @Select("select id, article_title, article_sign from article where author_id = #{authorId}")
    List<ArticlePageDTO> getTitlesByAuthorId(@Param("authorId") int authorId);

    @Select("select count(id) from article where author_id = #{authorId}")
    int countByAuthorId(@Param("authorId") int authorId);

    @Select("select coalesce(sum(char_length(article_content)), 0) from article where author_id = #{authorId}")
    long sumWordsByAuthorId(@Param("authorId") int authorId);

    @Select("select coalesce(sum(likes), 0) from article where author_id = #{authorId}")
    int sumLikesByAuthorId(@Param("authorId") int authorId);

    @Select("select coalesce(sum(favorites), 0) from article where author_id = #{authorId}")
    int sumFavoritesByAuthorId(@Param("authorId") int authorId);

    @Select("select article_sign as category, count(id) as count from article where author_id = #{authorId} group by article_sign")
    List<DashboardDTO.CategoryStat> getCategoryDistribution(@Param("authorId") int authorId);

    @Select("select date_format(create_time, '%Y-%m-%d') as month, count(id) as count from article where author_id = #{authorId} group by date_format(create_time, '%Y-%m-%d') order by month")
    List<DashboardDTO.DailyStat> getDailyTrend(@Param("authorId") int authorId);

    @Select("select id, article_title as title, likes, favorites from article where author_id = #{authorId} order by likes desc limit 5")
    List<DashboardDTO.TopArticle> getTopArticles(@Param("authorId") int authorId);
}
