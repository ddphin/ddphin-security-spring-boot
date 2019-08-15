package com.ddphin.security.demo.mapper;

import com.ddphin.security.endpoint.entity.UserIdentifier;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * ClassName: AUserIdentifierMapper
 * Function:  AUserIdentifierMapper
 * Date:      2019/6/17 下午2:59
 * Author     ddphin
 * Version    V1.0
 */

@Mapper
public interface AUserIdentifierMapper {
    Integer insert(@Param("userId") Long userId, @Param("identifierType") Integer identifierType, @Param("identifierValue") String identifierValue);
    UserIdentifier query(@Param("identifierType") Integer identifierType, @Param("identifierValue") String identifierValue);
}
