package com.ddphin.security.demo.mapper;

import com.ddphin.security.endpoint.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * ClassName: APermissionMapper
 * Function:  APermissionMapper
 * Date:      2019/6/17 下午2:59
 * Author     ddphin
 * Version    V1.0
 */

@Mapper
public interface APermissionMapper {
    List<Permission> queryAllPermission();
    List<Long> queryPermissionIdList(@Param("userId") Long userId);
}
