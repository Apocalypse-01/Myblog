package liyue.myblog.mapper;

import liyue.myblog.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    /**
     * 根据 email 查询密码
     * @param email
     * @return
     */
    @Select("select password from user where email=#{email}")
    String getPSByEmail(String email);

    /**
     * 添加用户
     * @param userDTO
     */
    void insertUser(UserDTO userDTO);

    /**
     * 根据 email 查询用户信息
     * @return
     */
    UserDTO getByEmail(String email);

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    @Select("select id,user_name,email,phone,signature,avatar from user where id = #{userId}")
    UserDTO getUserInfor(Integer userId);

    void updateUser(UserDTO userDTO);
}
