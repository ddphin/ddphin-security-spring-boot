package com.ddphin.security.demo.mapper;

import com.ddphin.security.endpoint.entity.UserCredential;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * ClassName: AUserCredentialMapper
 * Function:  AUserCredentialMapper
 * Date:      2019/6/17 下午2:59
 * Author     ddphin
 * Version    V1.0
 */

@Mapper
public interface AUserCredentialMapper {
    UserCredential query(@Param("userId") Long userId, @Param("credentialType") Integer credentialType);
}
