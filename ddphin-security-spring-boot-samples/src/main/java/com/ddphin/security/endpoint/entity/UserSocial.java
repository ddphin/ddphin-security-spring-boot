package com.ddphin.security.endpoint.entity;

import com.ddphin.security.endpoint.entity.ASocial;
import lombok.Data;

import java.util.Date;

/**
 * ClassName: UserSocial
 * Function:  UserSocial
 * Date:      2019/6/17 下午2:48
 * Author     ddphin
 * Version    V1.0
 */
@Data
public class UserSocial implements ASocial {
    private Long id;
    private Long userId;
    private Integer identifierType;
    private Integer socialType;
    private String socialValue;
    private String accessToken;
    private String refreshToken;
    private Date expireTime;
    private String sessionKey;
    private String avatar;
    private String name;
    private Integer gender;
}
