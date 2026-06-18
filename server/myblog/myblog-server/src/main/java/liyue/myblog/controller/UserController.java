package liyue.myblog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import liyue.myblog.dto.UserDTO;
import liyue.myblog.entity.User;
import liyue.myblog.result.Result;
import liyue.myblog.service.UserService;
import liyue.myblog.utils.JwtUtils;
import liyue.myblog.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


@RestController
@RequestMapping("/user")
@Tag(name = "用户模块")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    @Operation(summary = "用户登陆")
    public Result<UserDTO> userLogin(@RequestBody User user) {         // 默认@RequestParam 注解 接受实体类 接受以表单形式传输的实体属性

        // 判断 用户名和密码是否正确
        boolean isPass = userService.isPass(user.getEmail(), user.getPassword());

        System.out.println(user);
        System.out.println(isPass);

        //正确，生成token
        if (isPass) {

            //获取用户信息
            log.info("用户名密码正确:{}");
            JwtUtils jwt = new JwtUtils();
            String token = jwt.JWTCreate(user);

            //将用户信息传递给前端，不重要的 （id,邮箱，手机号）
            UserDTO userDTO=userService.getUserByEmail(user.getEmail());
            userDTO.setToken(token);
            return Result.success(userDTO);
        } else {

            return Result.error("用户名或......密码不正确");
        }

    }

    /**
     * @return
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Result<UserVO> userRegister(@RequestBody UserVO userVO) {    // 默认@RequestParam 注解 接受实体类 接受以表单形式传输的实体属性

        log.info("用户: {}" + userVO);

        //添加用户到数据库，返回 false 表示邮箱已被占用
        boolean success = userService.insertUser(userVO);
        if (!success) {
            return Result.error("该邮箱已被注册");
        }

        return Result.success(userVO);
    }

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    @GetMapping("/getUserInfo")
    public Result<UserDTO> getUserInfor(@RequestParam("userId") Integer userId){

        log.info("userId:{}",userId);
        UserDTO userDTO=userService.getUserInfor(userId);

        return Result.success(userDTO);
        
    }

    @PutMapping("/updateUserInfo")
    @Operation(summary = "更新用户信息")
    public Result<UserDTO> updateUserInfo(@RequestBody UserDTO userDTO){
        log.info("更新用户信息:{}", userDTO);
        if(userDTO.getId() == 0){
            return Result.error("用户ID不能为空");
        }
        userService.updateUser(userDTO);
        UserDTO updated = userService.getUserInfor(userDTO.getId());
        return Result.success(updated);
    }

    @PostMapping("/uploadAvatar")
    @Operation(summary = "上传头像")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file,
                                       @RequestParam("userId") String userId) {
        if (file.isEmpty()) {
            return Result.error("文件不能为空");
        }
        if (file.getSize() > 2 * 1024 * 1024) {
            return Result.error("文件大小不能超过2MB");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return Result.error("仅支持图片文件");
        }

        String originalFilename = file.getOriginalFilename();
        String ext = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String newFilename = UUID.randomUUID().toString().replace("-", "") + ext;

        String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator + "avatar";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filePath = uploadDir + File.separator + newFilename;
        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            log.error("头像上传失败:{}", e.getMessage());
            return Result.error("头像上传失败");
        }

        String avatarUrl = "/uploads/avatar/" + newFilename;
        UserDTO userDTO = new UserDTO();
        userDTO.setId(Integer.parseInt(userId));
        userDTO.setAvatar(avatarUrl);
        userService.updateUser(userDTO);

        log.info("头像上传成功: userId={}, avatarUrl={}", userId, avatarUrl);
        return Result.success(avatarUrl);
    }





    @GetMapping("/test")
    @Operation(summary = "test")
    public Result test() {

        log.info("测试类启用了......");
        return Result.success();
    }

}
