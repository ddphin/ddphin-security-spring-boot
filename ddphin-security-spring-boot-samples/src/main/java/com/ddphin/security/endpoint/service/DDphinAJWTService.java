package com.ddphin.security.endpoint.service;

import com.ddphin.security.demo.system.redis.helper.RedisHelper;
import com.ddphin.security.entity.AIdentity;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

/**
 * JWTServiceImpl
 *
 * @Date 2019/7/18 下午5:31
 * @Author ddphin
 */
@Service
public class DDphinAJWTService extends AJWTAbstractService {
    private static final Integer JWT_EXPIRE_DAYS = 6;

    @Autowired
    private RedisHelper redisHelper;
    
    @Override
    protected void saveToken(String id, String token) {
        Date now = DateTime.now().toDate();
        Date expireTime = DateTime.now().plusDays(JWT_EXPIRE_DAYS).toDate();
        redisHelper.forValue().set(id, token, Duration.ofMillis(expireTime.getTime() - now.getTime()));
    }

    @Override
    protected void removeToken(String id) {
        redisHelper.redis().delete(id);
    }

    @Override
    protected String queryToken(String id) {
        return (String) redisHelper.forValue().get(id);
    }

    @Override
    protected String getJWTID(AIdentity identity) {
        return "_token_@uid=" + identity.getUserId();
    }
}
