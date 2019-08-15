package com.ddphin.security.demo.mapper;

import com.ddphin.security.endpoint.entity.SocialDetail;
import com.ddphin.security.endpoint.entity.UserSocial;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * ClassName: AUserSocialMapper
 * Function:  AUserSocialMapper
 * Date:      2019/6/17 下午2:59
 * Author     ddphin
 * Version    V1.0
 */

@Mapper
public interface AUserSocialMapper {
    UserSocial query(@Param("userId") Long userId, @Param("identifierType") Integer identifierType, @Param("socialType") Integer socialType);
    Integer insert(@Param("userId") Long userId, @Param("identifierType") Integer identifierType, @Param("socialType") Integer socialType, @Param("socialDetail") SocialDetail socialDetail);
    Integer update(@Param("userId") Long userId, @Param("identifierType") Integer identifierType, @Param("socialType") Integer socialType, @Param("socialDetail") SocialDetail socialDetail);
}
