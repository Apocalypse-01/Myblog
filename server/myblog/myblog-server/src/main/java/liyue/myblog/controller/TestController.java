package liyue.myblog.controller;

import liyue.myblog.entity.User;
import liyue.myblog.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@Slf4j
public class TestController {
    @PostMapping("/login")
    public String login(@RequestParam("name") String name) {
        User user = new User();
        user.setName(name);
        user.setId(6);

        JwtUtils jwtUtils = new JwtUtils();

        return jwtUtils.JWTCreate(user);
    }

    // 需要 Token 验证的接口
    @GetMapping("/data")
    public String getData() {
        log.info("发出数据了...");
        return "敏感数据";
    }
}
