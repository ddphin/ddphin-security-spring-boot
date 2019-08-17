# Security 通用安全框架
```$xslt
        <dependency>
            <groupId>com.github.ddphin</groupId>
            <artifactId>ddphin-security-spring-boot-starter</artifactId>            
            <version>1.0.2</version>
        </dependency>
```
## 功能
`ddphin-security` 基于`spring-security`提供了一个公共的安全框架
- 登录与注册
    - 手机号登录
        - 密码登录
        - 验证码登录
      <br>如果验证通过但手机尚未注册则自动注册
    - 微信登录
       <br>如果微信用户尚未注册则自动基于微信开放平台的union机制注册
      - 微信移动端授权登录
      - 微信服务号授权登录
      - 微信订阅号授权登录
      - 微信小程序授权登录
      - 微信 H5 授权登录
    - QQ 登录
       <br>如果 QQ 用户尚未注册则自动基于腾讯开放平台的union机制注册
      - QQ 移动端授权登录
      - QQ H5授权登录
- JWT
  <br>登录成功之后，获取用户权限，基于 JWT 生成 token      
- 权限
  <br>请求头携带token，根据请求URL（如：/user/account/*等）和METHOD（如：GET/PUT/POST等），校验是否有权限访问
- 获取当前用户
  <br>登录和JWT认证之后，可在当前线程任何地方获取到当前用户<br>
  `(AIdentity) SecurityContextHolder.getContext().getAuthentication().getDetails()`

## 服务端使用
- 配置
   - login： 登录url
   - logout：登出url
   - permissive： 无需登录即可访问的url（列表）
    ```$xslt
    spring:
      security:
        authorize:
          login: /login
          logout: /logout
          permissive:
            - /tourist/*
    ```
-  实现以下三类接口(抽象类)
   - entity
     <br>认证所需的实体类
     - `AIdentifier`：用户唯一标识接口
        ```$xslt
        public interface AIdentifier {
            Long getUserId();           // 用户ID
            Integer getIdentifierType();// 用户唯一标识类型
            String getIdentifierValue();// 用户唯一标识
        }
        ```
        `IdentifierType`用户唯一标识类型对应以下枚举
        ```$xslt
        public enum AIdentifierType {
            MOBILE,// 手机号
            WX,    // 微信
            QQ;    // QQ
        }
        ```
     - `ACredential`:用户凭据
        ```$xslt
        public interface ACredential {
            Long getUserId();           // 用户ID
            Integer getCredentialType();// 用户凭据类型
            String getCredentialValue();// 用户凭据
        }
        ```
        `CredentialType`用户凭据类型对应以下枚举
        ```$xslt
        public enum ACredentialType {
            PASSWORD,  // 密码
            VALID_CODE,// 验证码
            GRANT_CODE,// 授权码
        }
        ```
     - `ASocial`:用户社交账号信息
        ```$xslt
        public interface ASocial {
            Long getUserId();           // 用户ID
            Integer getIdentifierType();// 用户唯一标识类型
            Integer getSocialType();    // 用户社交账号类型
            String getSocialValue();    // 用户社交账号
        }
        ```
        `SocialType`用户社交账号类型对应以下枚举
        ```$xslt
        public enum ASocialType {
            WX_APP_OPENID,// 微信移动端
            WX_H5_OPENID, // 微信 H5 端
            WX_XCX_OPENID,// 微信小程序端
            WX_SUB_OPENID,// 微信订阅号端
            WX_SRV_OPENID,// 微信服务号端
        
            QQ_APP_OPENID,// QQ 移动端
            QQ_H5_OPENID; // QQ H5 端
        ```
     - `APermission`:权限
        ```$xslt
        public interface APermission {
            String getPermissionId(); // 权限ID
            String getRequestUrl();   // URL：如/user/account/*
            String getRequestMethod();// METHOD: 如 GET/PUT/POST/DELETE
        }
        ```
        `RequestUrl`即 HTTP 请求的URL模式，如`/user/account/*`<br>
        `RequestMethod`即 HTTP 请求的METHOD:`GET/PUT/POST/DELETE`等    
     - `ASocialDetail`:通过`ASocialProviderRegister`从从第三方社交平台获取社交账号详情
        ```$xslt
        public interface ASocialDetail {
            String getUnionid();// IdentifierType对应的唯一标识
            String getOpenid(); // SocialType对应的唯一标识
        }
        ```         
   - service： 认证所需的服务
     - AuthenticationService： 认证服务
     ```$xslt
     public interface AuthenticationService {
         // 查询Identifier
         AIdentifier queryIdentifier(Integer identifierType, String identifierValue);
         // 保存Identifier，自动注册时需要
         void saveIdentifier(Long userId, Integer identifierType, String identifierValue);
     
         // 查询Credential
         ACredential queryCredential(Long userId, Integer credentialType);
     
         // 查询用户的Permission
         List<String> queryPermissionIdList(Long userId);
         // 查询所有的Permission
         List<? extends APermission> queryAllPermission();
     
         // 查询ValidCode
         String queryValidCode(String mobile);
         // 移除ValidCode，ValidCode验证成功之后需要
         void removeValidCode(String mobile);
     
         // 获取一个新的UserID,自动注册时需要
         Long nextUserId();
         // 保存用户信息： 邀请码，手机号,自动注册时需要
         void saveUser(Long userId, String invitationCode, String mobile);
         // 保存用户信息： 邀请码，社交张账号信息,自动注册时需要
         void saveUser(Long userId, String invitationCode, ASocialDetail socialDetail);
     
         // 查询用户已保存的Social信息
         ASocial querySocial(Long userId, Integer identifierType, Integer socialType);
         // 保存社交账号信息，自动注册时需要
         void saveSocial(Long userId, Integer identifierType, Integer socialType, ASocialDetail socialDetail);
         // 更新社交账号信息，可选
         void updateSocial(Long userId, Integer identifierType, Integer socialType, ASocialDetail socialDetail);
     }
     ```
     - AJWTAbstractService： JWT服务
     ```$xslt
     public abstract class AJWTAbstractService {
        // 保存token
        protected abstract void saveToken(String id, String token);
        // 移除token
        protected abstract void removeToken(String id);
        // 查询token
        protected abstract String queryToken(String id);
        // 获取token id
        protected abstract String getJWTID(AIdentity identity);
     }
     ```     
   - social: 社交账号信息提供服务
     ```$xslt
     public abstract class ASocialProviderRegister {
        // 支持的社交账号类型
        ASocialType socialType();
        // 获取社交账号信息：
        // code: 授权码
        // socialExtra: 请求数据
        ASocialDetail querySocialDetail(String code, Map<String, Object> socialExtra);
     }
     ```
## 前端使用
### 登录请求参数
- identifierType: 用户唯一标识类型
    - 必填
    - 有效值为：0、1、2
    <br>即AIdentifierType 各枚举的 ordinal() 方法返回值
- identifierValue: 用户唯一标识
    - 必填
    - 有效值为
        - identifierType=0(MOBILE)时：手机号
        - identifierType=1(WX)时：0、1、2、3、4
        <br>即ASocialType枚举中的WX_APP_OPENID、WX_H5_OPENID、WX_XCX_OPENID、WX_SUB_OPENID、WX_SRV_OPENID的 ordinal() 方法返回值
        <br>也可以自定义
        - identifierType=2(QQ)时：5、6
        <br>即ASocialType枚举中的QQ_APP_OPENID、QQ_H5_OPENID的 ordinal() 方法返回值
        <br>也可以自定义
- credentialType: 用户凭据类型
    - 必填
    - 有效值为：0、1、2
    <br>即ACredentialType 各枚举的 ordinal() 方法返回值
- credentialValue: 用户凭据
    - 必填
    - 有效值为
        - ACredentialType=0(PASSWORD)时：密码
        - ACredentialType=1(VALID_CODE)时：验证码
        - ACredentialType=2(GRANT_CODE)时：授权码
- data: 扩展数据，QQ或微信登录时传递
    - 可选
    - 有效值：JSON
- invitationCode: 邀请码，注册时传递
    - 可选
    - 有效值：字符串
### 登录成功返回参数
返回 Http Header 包含：
- Authorization: Bearer xxxxxxxxxxxxxxxxjwtxxxxxxxxxxxxxx
### 身份和权限认证参数
请求 Http Header 包含：
- Authorization: Bearer xxxxxxxxxxxxxxxxjwtxxxxxxxxxxxxxx
## 举例
[请查看 ddphin-security-spring-boot-samples](https://github.com/ddphin/ddphin-security-spring-boot/tree/master/ddphin-security-spring-boot-samples)
