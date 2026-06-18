package liyue.myblog.mapper;

import liyue.myblog.dto.CommentDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("insert into article_comment (article_id, user_id, user_name, content, create_time, change_time) " +
            "values (#{articleId}, #{userId}, #{userName}, #{content}, #{createTime}, #{changeTime})")
    void insertComment(CommentDTO commentDTO);

    @Select("select id, article_id as articleId, user_id as userId, user_name as userName, content, create_time as createTime " +
            "from article_comment where article_id = #{articleId} order by create_time asc")
    List<CommentDTO> getCommentsByArticleId(Integer articleId);

    @Delete("delete from article_comment where id = #{id} and user_id = #{userId}")
    void deleteComment(@Param("id") Integer id, @Param("userId") Integer userId);

    @Select("select count(id) from article_comment where article_id = #{articleId}")
    int getCommentCountByArticleId(Integer articleId);
}
