package edu.cuz.mamv2.controller;

import cn.hutool.core.util.IdUtil;
import edu.cuz.mamv2.utils.BackEnum;
import edu.cuz.mamv2.utils.BackMessage;
import edu.cuz.mamv2.utils.SecretUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author VM
 * @date 2022/1/19 14:11
 * @description
 */
@Slf4j(topic = "登录控制器")
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redis;

    @GetMapping("/getrsa")
    public BackMessage getRsaKey() {
        // 生成非对称秘钥
        KeyPair keyPair = SecretUtils.getRSAKeys(2048);
        // 保存私钥到redis中，过期时间为一天
        String uuid = IdUtil.simpleUUID();
        redis.boundValueOps(uuid).set(keyPair.getPrivate(), 1, TimeUnit.DAYS);
        // 返回公钥和公钥id
        HashMap<String, String> resData = new HashMap<>(2);
        resData.put("keyId", uuid);
        resData.put("publicKey", Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        return new BackMessage(BackEnum.SUCCESS, resData);
    }

    @GetMapping("/getaeskey")
    public BackMessage getAesKey(String keyId, String clientKey) {
        PrivateKey serverPrivateKey = (PrivateKey) redis.boundValueOps(keyId).get();
        if (serverPrivateKey != null) {
            String s = clientKey.replaceAll(" ", "+");
            String decryptClientPublicKey = SecretUtils.rsaDecrypt(s, serverPrivateKey);
            // 生成对称密钥
            String encodeKey = SecretUtils.getAesKey();
            String uuid = IdUtil.simpleUUID();
            redis.boundValueOps(uuid).set(encodeKey);
            log.info("对称秘钥：", encodeKey);
            // 加密对称密钥
            String encryptAesKey = SecretUtils.rsaEncrypt(encodeKey, decryptClientPublicKey);
            // 返回加密的aes对称秘钥和秘钥id
            HashMap<String, String> resData = new HashMap<>(2);
            resData.put("secretKeyId", uuid);
            resData.put("secretKey", encryptAesKey);
            return new BackMessage(BackEnum.SUCCESS, resData);
        } else {
            return new BackMessage(BackEnum.FORBIDDEN);
        }
    }
}
