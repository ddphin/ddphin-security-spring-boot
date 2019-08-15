package com.ddphin.security.endpoint.social;

import com.alibaba.fastjson.JSONObject;
import com.ddphin.security.demo.util.AESCryptor;
import com.ddphin.security.demo.util.HttpRequestor;
import com.ddphin.security.endpoint.entity.ASocialDetail;
import com.ddphin.security.endpoint.entity.SocialDetail;
import com.ddphin.security.entity.ASocialType;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * ASocialProviderWXXCX
 *
 * @Date 2019/7/21 下午8:34
 * @Author ddphin
 */
@Component
public class SocialProviderWXXCX extends ASocialProviderRegister {
    @Value("${spring.security.social.wx.xcx.appId}")
    private String APP_ID;
    @Value("${spring.security.social.wx.xcx.appSecret}")
    private String APP_SECRET;

    @Override
    public ASocialType socialType() {
        return ASocialType.WX_XCX_OPENID;
    }

    @Override
    public ASocialDetail querySocialDetail(String code, Map<String, Object> credentialExtra) {
        String requestUrl = "https://api.weixin.qq.com/sns/jscode2session?grant_type=authorization_code"
                + "&appId=" + APP_ID
                + "&secret=" + APP_SECRET
                + "&js_code=" + code;
        try {
            String data = HttpRequestor.doGet(requestUrl);
            JSONObject json = JSONObject.parseObject(data);
            String unionid = json.getString("unionid");
            String openid = json.getString("openid");
            String name = json.getString("nickname");
            String avatar = json.getString("figureurl_qq_2");
            Integer gender = json.getInteger("gender");
            String sessionKey = json.getString("session_key");

            String wxXcxEncryptedData = MapUtils.getString(credentialExtra, "data");
            String wxXcxEncryptedIv = MapUtils.getString(credentialExtra, "iv");
            if (StringUtils.isNotBlank(wxXcxEncryptedData) && StringUtils.isNotBlank(wxXcxEncryptedIv)) {
                String xcxdata = AESCryptor.cryptor.decrypt(wxXcxEncryptedData, sessionKey, wxXcxEncryptedIv);
                JSONObject xcxjson = JSONObject.parseObject(xcxdata);

                unionid = xcxjson.getString("unionId");
                openid = xcxjson.getString("openId");
                name = xcxjson.getString("nickName");
                avatar = xcxjson.getString("avatarUrl");
                gender = xcxjson.getInteger("gender");
            }
            SocialDetail info = new SocialDetail();
            info.setUnionid(unionid);
            info.setOpenid(openid);
            info.setSessionKey(sessionKey);
            info.setName(name);
            info.setAvatar(avatar);
            info.setGender(gender);
            return info;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
