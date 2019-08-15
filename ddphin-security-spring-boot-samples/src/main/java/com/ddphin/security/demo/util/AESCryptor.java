package com.ddphin.security.demo.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Objects;

public class AESCryptor {
	
	public static final AESCryptor cryptor = new AESCryptor();

    private static boolean initialized = false;

    private static final String ALGORITHM_AES = "AES";
    private static final String PROVIDER_BC= "BC";
    private static final String TRANSFORMATION_AES_CBC_PKCS7PADDING = "AES/CBC/PKCS7Padding";

    public String decrypt(String content, String key, String iv) {
        return new String(
                Objects.requireNonNull(this.decrypt(
                        Base64.decode(content),
                        Base64.decode(key),
                        Base64.decode(iv)
                )),
                StandardCharsets.UTF_8);
    }

    /**
     * AES解密
     * @param content 密文
     * @return 解密后的字符串
     */
    private byte[] decrypt(byte[] content, byte[] keyByte, byte[] ivByte) {
        initialize();
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION_AES_CBC_PKCS7PADDING, PROVIDER_BC);
            Key sKeySpec = new SecretKeySpec(keyByte, ALGORITHM_AES);

            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIV(ivByte));// 初始化
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initialize(){
        if (!initialized) {
        	Security.addProvider(new BouncyCastleProvider());
            initialized = true;
        }
    }
    //生成iv
    private AlgorithmParameters generateIV(byte[] iv) throws Exception {
        AlgorithmParameters params = AlgorithmParameters.getInstance(ALGORITHM_AES);
        params.init(new IvParameterSpec(iv));
        return params;
    }
}
