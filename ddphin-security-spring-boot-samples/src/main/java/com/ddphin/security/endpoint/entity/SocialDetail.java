package com.ddphin.security.endpoint.entity;

import com.ddphin.security.endpoint.entity.ASocialDetail;
import lombok.Data;

import java.util.Date;

/**
 * SocialDetail
 *
 * @Date 2019/8/15 上午11:19
 * @Author ddphin
 */
@Data
public class SocialDetail implements ASocialDetail {
    private String unionid;
    private String openid;
    private String accessToken;
    private String refreshToken;
    private Date expireTime;
    private String sessionKey;
    private String avatar;
    private String name;
    private Integer gender;
}
