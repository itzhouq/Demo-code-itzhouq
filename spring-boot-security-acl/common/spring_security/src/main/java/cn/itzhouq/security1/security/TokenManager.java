package cn.itzhouq.security1.security;

import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author zhouquan
 * @date 2021/2/14 14:52
 */
@Component
public class TokenManager {

    // oken编码时长
    private static final long expireToken = 24 * 60 * 60 * 1000;

    // 编码秘钥
    private static final String tokenSignKey = "123456";

    // 1. 使用JWT根据用户名生成token
    public String createToken(String username) {
        return Jwts.builder().setSubject(username).setExpiration(new Date((System.currentTimeMillis()) + expireToken))
                .signWith(SignatureAlgorithm.HS512, tokenSignKey).compressWith(CompressionCodecs.GZIP).compact();
    }

    // 2. 根据token字符串得到用户信息
    public String getUserInfoFromToken(String token) {
        return Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token).getBody().getSubject();
    }

    // 3. 删除token
    public void removeToken(String token) {

    }
}
