package liyue.myblog.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.micrometer.common.util.StringUtils;
import liyue.myblog.entity.User;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
public class JwtUtils {

    //密匙
    private static final String SECRET_KEY="mySecretKey";

    //过期时间
    private static final int EXPIRATION_TIME = 3600 * 1000;

    /**
     * 对数据进行jwt加密
     * @param user
     * @return
     */
    public String JWTCreate(User user) {

        //header    头部  -- 指定算法  'alg' = HS256         'typ'-- 'JTW'
        //是Map集合
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");
//
        log.info("user:{}",user);
//
        //payload   负载      传过来的用户信息 等等
        Map<String, Object> payload = new HashMap<>();
        payload.put("email", user.getEmail());
        payload.put("password", user.getPassword());


        //定义 token 有效时间
        Calendar overTime = Calendar.getInstance();           //获取当前时间
        overTime.add(Calendar.SECOND, EXPIRATION_TIME);       //定义截至时间  SECOND -- 秒

        //signature   ==>  利用 header，payload，secret 和相应算法进行生成
        //利用 Jwts 构建token
        String token = JWT.create()
                .withHeader(header)
                .withPayload(payload)
                .withExpiresAt(overTime.getTime())
                .sign(Algorithm.HMAC256(SECRET_KEY));
//
       log.info("token:{}",token);
//
        return token;
    }

    /**
     * 对token进行解析
     *
     * @param token
     * @return
     */
    public static DecodedJWT JWTParser(String token) {

        try {

            System.out.println(token);
            //解析 token
            DecodedJWT jwtParser = JWT.decode(token);
            
            //返回 jwtParser
            return jwtParser;

        }catch (Exception exception){

            System.out.println("解析token出问题--JWTParser");
            throw exception;
        }
    }

    /**
     * 判断 token是否过期，是否有效
     * @param token
     * @return
     */
    public boolean isEffect(String token) {
        if (StringUtils.isEmpty(token)){
            return false;
        }
        try {
            //解析token
            DecodedJWT jwtParser = JWT.decode(token);

            //获取过期时间，并判断是否过期，过期则不能登陆
            //到期时间
            Date overTime = jwtParser.getExpiresAt();

            //当前时间
            Calendar nowTime = Calendar.getInstance();

            //判断是否到当前时间
            boolean isOver = overTime.before(nowTime.getTime());

            //时间过期则返回false
            if (isOver){
                System.out.println("超出时间");
                return false;
            }

        }catch (Exception exception){
            //如果解析失败 则抛出异常
            System.out.println("jwt的token超时--isEffect");
            throw  exception;
        }

        return true;
    }

}





