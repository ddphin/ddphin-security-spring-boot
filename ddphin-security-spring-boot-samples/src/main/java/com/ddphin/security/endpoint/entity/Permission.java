package com.ddphin.security.endpoint.entity;

import lombok.Data;

/**
 * Permission
 *
 * @Date 2019/8/15 下午12:38
 * @Author ddphin
 */
@Data
public class Permission implements APermission {
    private Long id;
    private String requestUrl;
    private String requestMethod;

    @Override
    public String getPermissionId() {
        return String.valueOf(this.id);
    }
}
