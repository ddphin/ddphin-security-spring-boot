package com.ddphin.security.endpoint.entity;

import com.ddphin.security.endpoint.entity.AIdentifier;
import lombok.Data;

/**
 * ClassName: UserIdentifier
 * Function:  UserIdentifier
 * Date:      2019/6/17 下午2:48
 * Author     ddphin
 * Version    V1.0
 */
@Data
public class UserIdentifier implements AIdentifier {
    private Long id;
    private Long userId;
    private Integer identifierType;
    private String identifierValue;
}
