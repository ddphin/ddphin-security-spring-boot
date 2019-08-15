package com.ddphin.security.endpoint.entity;

import com.ddphin.security.endpoint.entity.ACredential;
import lombok.Data;

/**
 * ClassName: UserCredential
 * Function:  UserCredential
 * Date:      2019/6/17 下午2:48
 * Author     ddphin
 * Version    V1.0
 */
@Data
public class UserCredential implements ACredential {
    private Long id;
    private Long userId;
    private Integer credentialType;
    private String credentialValue;
}
