package liyue.myblog.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User{

    private int id;

    //邮箱
    private  String email;

    //姓名
    private String name;

    //密码
    private String password;

    //手机号
    private String phone;

    //个性签名   -------- 暂代

    //注册时间
    private LocalDateTime createTime;


}

