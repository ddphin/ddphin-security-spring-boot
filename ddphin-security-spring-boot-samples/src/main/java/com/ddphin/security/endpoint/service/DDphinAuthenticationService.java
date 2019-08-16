package com.ddphin.security.endpoint.service;

import com.alibaba.fastjson.JSONObject;
import com.ddphin.security.demo.mapper.APermissionMapper;
import com.ddphin.security.demo.mapper.AUserCredentialMapper;
import com.ddphin.security.demo.mapper.AUserIdentifierMapper;
import com.ddphin.security.demo.mapper.AUserSocialMapper;
import com.ddphin.security.demo.system.id.IDWorkerAware;
import com.ddphin.security.demo.system.redis.helper.RedisHelper;
import com.ddphin.security.endpoint.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * MyAuthenticationService
 *
 * @Date 2019/8/15 上午11:26
 * @Author ddphin
 */
@Slf4j
@Service
public class DDphinAuthenticationService extends IDWorkerAware implements AuthenticationService {
    @Autowired
    private RedisHelper redisHelper;

    @Autowired
    private AUserCredentialMapper aUserCredentialMapper;

    @Autowired
    private AUserIdentifierMapper aUserIdentifierMapper;

    @Autowired
    private AUserSocialMapper aUserSocialMapper;

    @Autowired
    private APermissionMapper aPermissionMapper;

    @Override
    public AIdentifier queryIdentifier(Integer identifierType, String identifierValue) {
        return aUserIdentifierMapper.query(identifierType, identifierValue);
    }

    @Override
    public ACredential queryCredential(Long userId, Integer credentialType) {
        return aUserCredentialMapper.query(userId, credentialType);
    }

    @Override
    public List<String> queryPermissionIdList(Long userId) {
        List<Long> list = aPermissionMapper.queryPermissionIdList(userId);
        return null == list ? null : list.stream().map(String::valueOf).collect(Collectors.toList());
    }

    @Override
    public List<? extends APermission> queryAllPermission() {
        return aPermissionMapper.queryAllPermission();
    }

    @Override
    public String queryValidCode(String mobile) {
        return (String) redisHelper.forValue().get("_valid_code_@mobile="+mobile);
    }

    @Override
    public void removeValidCode(String mobile) {
        redisHelper.redis().delete("_valid_code_@mobile="+mobile);
    }

    @Override
    public Long nextUserId() {
        return this.nextId();
    }

    @Override
    public void saveUser(Long userId, String invitationCode, String mobile) {
        log.info("saveUser:\n" +
                "    userId:{}\n" +
                "    mobile:{}", userId, mobile);
    }

    @Override
    public void saveUser(Long userId, String invitationCode, ASocialDetail socialInfo) {
        log.info("saveUser:\n" +
                "    userId:{}\n" +
                "    social:{}", userId, JSONObject.toJSONString(socialInfo));
    }

    @Override
    public void saveIdentifier(Long userId, Integer identifierType, String identifierValue) {
        aUserIdentifierMapper.insert(userId, identifierType, identifierValue);
    }

    @Override
    public ASocial querySocial(Long userId, Integer identifierType, Integer socialType) {
        return aUserSocialMapper.query(userId, identifierType, socialType);
    }

    @Override
    public void saveSocial(Long userId, Integer identifierType, Integer socialType, ASocialDetail socialInfo) {
        aUserSocialMapper.insert(userId, identifierType, socialType, (SocialDetail) socialInfo);
    }

    @Override
    public void updateSocial(Long userId, Integer identifierType, Integer socialType, ASocialDetail socialInfo) {
        aUserSocialMapper.update(userId, identifierType, socialType, (SocialDetail) socialInfo);
    }
}
