package liyue.myblog.interceptor;


import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import liyue.myblog.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private static final List<String> EXCLUDE_PATHS = List.of(
            "/user/register", "/user/login", "/user/test",
            "/comment/list", "/comment/count"
    );

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String requestURI = request.getRequestURI();
        log.info("Jwt拦截器启动......请求路径: {}", requestURI);

        for (String excludePath : EXCLUDE_PATHS) {
            if (requestURI.equals(excludePath)) {
                log.info("白名单路径，放行: {}", requestURI);
                return true;
            }
        }

        if (requestURI.startsWith("/article/") || requestURI.startsWith("/swagger") || requestURI.startsWith("/v3/") || requestURI.startsWith("/doc.html") || requestURI.startsWith("/webjars/") || requestURI.startsWith("/uploads/")) {
            log.info("白名单路径前缀，放行: {}", requestURI);
            return true;
        }

        if (requestURI.equals("/user/uploadAvatar")) {
            log.info("头像上传路径，放行: {}", requestURI);
            return true;
        }

        String token = request.getHeader("token");

        if (token == null) {
            log.info("token 不存在");
            return false;
        }

        try {
            JwtUtils jwtUtils = new JwtUtils();
            DecodedJWT jwtParser = jwtUtils.JWTParser(token);

            String signature = jwtParser.getSignature();
            log.info("签名密匙:{}", signature);

            log.info("token解析成功..--------------");
            return true;

        } catch (Exception e) {
            response.sendError(401, "Token 无效或已过期");
            return false;
        }

    }
}
