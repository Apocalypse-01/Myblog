package liyue.myblog.vo;

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
public class UserVO {

    //邮箱
    private  String email;

    //姓名
    private String userName;

    //密码
    private String password;

    //手机号
    private String phone;


}

