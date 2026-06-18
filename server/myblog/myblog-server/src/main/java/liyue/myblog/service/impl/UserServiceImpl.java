package liyue.myblog.service.impl;


import liyue.myblog.dto.UserDTO;
import liyue.myblog.mapper.UserMapper;
import liyue.myblog.service.UserService;
import liyue.myblog.utils.PasswordUtils;
import liyue.myblog.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class UserServiceImpl implements UserService {


    @Autowired
    UserMapper userMapper;

    /**
     * 判断用户名或密码死否正确
     *
     * @return
     */
    public boolean isPass(String email, String password) {
        String encodedPassword = userMapper.getPSByEmail(email);

        if (encodedPassword == null) {
            log.info("该账号不存在");
            return false;
        } else {
            if (PasswordUtils.verify(password, encodedPassword)) {
                log.info("密码正确---------:-");
                return true;
            } else {
                log.info("密码不正确--------");
                return false;
            }
        }

    }

    /**
     * 添加用户
     *
     * @param userVO
     */
    public boolean insertUser(UserVO userVO) {

        // 先检查邮箱是否已被注册
        UserDTO existUser = userMapper.getByEmail(userVO.getEmail());
        if (existUser != null) {
            log.warn("注册失败：邮箱已被注册 {}", userVO.getEmail());
            return false;
        }

        UserDTO userDTO = new UserDTO();

        BeanUtils.copyProperties(userVO, userDTO);

        userDTO.setPassword(PasswordUtils.encrypt(userDTO.getPassword()));
        userDTO.setCreateTime(LocalDateTime.now());

        log.info("userDTO:{}",userDTO);

        userMapper.insertUser(userDTO);

        return true;
    }

    /**
     * 根据 邮箱 查询用户信息
     * @return
     */
    public UserDTO getUserByEmail(String email) {

        UserDTO userDTO = userMapper.getByEmail(email);

        log.info("userDTO:P用户不重要的信息{}",userDTO);


        return userDTO;
    }

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    public UserDTO getUserInfor(Integer userId) {

        UserDTO userDTO = userMapper.getUserInfor(userId);

        log.info("userDTO:{}",userDTO);

        return userDTO;
    }

    public void updateUser(UserDTO userDTO) {
        userMapper.updateUser(userDTO);
        log.info("用户信息已更新:{}", userDTO);
    }


}
