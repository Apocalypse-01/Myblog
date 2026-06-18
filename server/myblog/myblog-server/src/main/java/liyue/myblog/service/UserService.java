package liyue.myblog.service;

import liyue.myblog.dto.UserDTO;
import liyue.myblog.vo.UserVO;

public interface UserService {


    /**
     * 判断用户名和密码是否正确
     * @return
     */
    boolean isPass(String email,String password);

    /**
     * 添加用户，返回 false 表示邮箱已被注册
     * @param userVO
     */
    boolean insertUser(UserVO userVO);

    /**
     * 根据 邮箱 查询用户信息
     * @return
     */
    UserDTO getUserByEmail(String email);

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    UserDTO getUserInfor(Integer userId);

    void updateUser(UserDTO userDTO);
}
