package com.ddphin.security.endpoint.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ddphin.security.demo.system.redis.helper.RedisHelper;
import com.ddphin.security.entity.AIdentifierType;
import com.ddphin.security.entity.AIdentity;
import com.ddphin.security.entity.ASocialType;
import com.ddphin.security.token.AIdentityAuthenticationToken;
import com.ddphin.security.token.AJwtAuthenticationToken;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

/**
 * JWTServiceImpl
 *
 * @Date 2019/7/18 下午5:31
 * @Author ddphin
 */
@Service
public class DDphinAJWTService implements AJWTService {
    private static final String JWT_TYP = "JWT";
    private static final String JWT_ALG = "HS256";
    private static final String JWT_ISSUER = "iCDD2019";
    private static final String JWT_HMAC256 = "iCDD2019";
    private static final Integer JWT_EXPIRE_DAYS = 6;

    @Autowired
    private RedisHelper redisHelper;

    @Override
    public String create() {
        try {
            AIdentityAuthenticationToken authentication = (AIdentityAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            AIdentity identity = (AIdentity) authentication.getDetails();

            return this.create(
                    identity,
                    authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new));
        } catch (JWTCreationException exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public String refresh() {
        try {
            AJwtAuthenticationToken authentication = (AJwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            AIdentity identity = (AIdentity) authentication.getDetails();
            String jwtID = this.getJWTID(identity);
            String token = (String) redisHelper.forValue().get(jwtID);
            if (null == token) {
                return this.create(
                        identity,
                        authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new));
            }
            else {
                Date now = DateTime.now().toDate();
                Date expireTime = DateTime.now().plusDays(JWT_EXPIRE_DAYS).toDate();
                redisHelper.forValue().set(jwtID, token, Duration.ofMillis(expireTime.getTime() - now.getTime()));
                return token;
            }
        } catch (JWTCreationException exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public void remove(String token) {
        DecodedJWT jwt = JWT.require(Algorithm.HMAC256(JWT_HMAC256))
                .withIssuer(JWT_ISSUER)
                .build()
                .verify(token);
        redisHelper.redis().delete(jwt.getId());
    }

    @Override
    public DecodedJWT validate(String token, String ip) {
        DecodedJWT jwt = JWT.require(Algorithm.HMAC256(JWT_HMAC256))
                .withIssuer(JWT_ISSUER)
                .withClaim("ip", ip)
                .build()
                .verify(token);
        String redisToken = (String) redisHelper.forValue().get(jwt.getId());
        if (token.equals(redisToken)) {
            return jwt;
        }
        else {
            return null;
        }
    }


    private String create(AIdentity identity, String[] authority) {
        try {
            Date now = DateTime.now().toDate();
            Date expireTime = DateTime.now().plusDays(JWT_EXPIRE_DAYS).toDate();

            String[] audience;
            if (null == identity.getSocialType()) {
                audience = new String[]{Objects.requireNonNull(AIdentifierType.fromCode(identity.getIdentifierType())).name()};
            }
            else {
                audience = new String[]{
                        Objects.requireNonNull(AIdentifierType.fromCode(identity.getIdentifierType())).name(),
                        Objects.requireNonNull(ASocialType.fromCode(identity.getSocialType())).name()
                };
            }
            String jwtID = this.getJWTID(identity);
            String token = JWT.create()
                    /*设置头部信息 Header*/
                    .withHeader(new HashMap<String, Object>(){{
                        put("alg", JWT_ALG);
                        put("typ", JWT_TYP);
                    }})
                    /*设置 载荷 Payload*/
                    .withJWTId(jwtID)
                    .withIssuer(JWT_ISSUER)//签名是有谁生成 例如 服务器
                    .withSubject(String.valueOf(identity.getUserId()))//签名的主题
                    .withAudience(audience)//签名的观众 也可以理解谁接受签名的
                    .withIssuedAt(now) //生成签名的时间
                    .withExpiresAt(DateTime.now().plusYears(100).toDate())//签名过期的时间
                    .withArrayClaim("Authority", authority)
                    .withClaim("ip", identity.getIp())
                    /*签名 Signature */
                    .sign(Algorithm.HMAC256(JWT_HMAC256));

            redisHelper.forValue().set(jwtID, token, Duration.ofMillis(expireTime.getTime() - now.getTime()));
            return token;
        } catch (JWTCreationException exception){
            exception.printStackTrace();
            return null;
        }
    }

    private String getJWTID(AIdentity identity) {
        StringBuilder sb = new StringBuilder();
        sb.append("_token_@");
        sb.append(identity.getIdentityAuthenticatorType());
        return sb.toString();
    }
}
