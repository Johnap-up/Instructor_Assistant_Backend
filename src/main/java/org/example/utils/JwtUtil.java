package org.example.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {
    //Jwt秘钥
    @Value("${spring.security.jwt.key}")
    private String key;
    @Value("${spring.security.jwt.expire}")
    private int expire;
    @Resource
    StringRedisTemplate template;

    public boolean invalidateJwt(String token){
        String str = this.convertToken(token);
        if (str == null) return false;
        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try {
            DecodedJWT jwt = jwtVerifier.verify(str);
            String id = jwt.getId();
            return deleteToke(id, jwt.getExpiresAt());
        }catch (JWTVerificationException e){
            return false;
        }
    }
    public boolean deleteToke(String uuid, Date time){
        if (isInvalidToken(uuid)) return true;          //如果已经存了，那就直接返回true
        long restTime = Math.max(time.getTime() - new Date().getTime(), 0);
        template.opsForValue().set(ConstUtil.JWT_BLACK_LIST + uuid, "", restTime, TimeUnit.MILLISECONDS);
        return true;
    }
    public boolean isInvalidToken(String uuid){
        return Boolean.TRUE.equals(template.hasKey(ConstUtil.JWT_BLACK_LIST + uuid));
    }
    public DecodedJWT resolveJwt(String token){
        String str = this.convertToken(token);
        if (str == null) return null;
        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try {
            DecodedJWT verify = jwtVerifier.verify(str);
            if(this.isInvalidToken(verify.getId()))
                return null;
            Date expireAt = verify.getExpiresAt();
            return new Date().after(expireAt) ? null : verify;
        }catch (JWTVerificationException e){
            return null;
        }
    }
    private String convertToken(String token){
        if (token == null || !token.startsWith("Bearer ")) return null;
        return token.substring("Bearer ".length());
    }

    //根据用户信息创建Jwt令牌
    public String createJwt(UserDetails user){
        Algorithm algorithm = Algorithm.HMAC256(key);
        Date expireTime = getExpireTime();
        return JWT.create()
                .withJWTId(UUID.randomUUID().toString())
                .withClaim("name", user.getUsername())  //配置JWT自定义信息
                .withClaim("id", 123)
                .withClaim("authorities", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .withExpiresAt(expireTime)  //设置过期时间
                .withIssuedAt(new Date())    //设置创建创建时间
                .sign(algorithm);   //最终签名
    }
    public Date getExpireTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 3600 * 24 * expire);
        return calendar.getTime();
    }

    public UserDetails toUser(DecodedJWT jwt){
        Map<String, Claim> claims = jwt.getClaims();
        return User
                .withUsername(claims.get("name").asString())
                .password("*******")        //暂时这样做
                .authorities(claims.get("authorities").asArray(String.class))
                .build();
    }
    public Integer toInt(DecodedJWT jwt){
        Map<String, Claim> claims = jwt.getClaims();
        return claims.get("id").asInt();
    }
}
