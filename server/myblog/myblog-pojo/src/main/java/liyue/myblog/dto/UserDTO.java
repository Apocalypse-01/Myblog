package liyue.myblog.dto;

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
public class UserDTO{

    private int id;

    private  String email;

    private String userName;

    private String password;

    private String phone;

    private String token;

    private LocalDateTime createTime;

    private String signature;

    private String avatar;
}

