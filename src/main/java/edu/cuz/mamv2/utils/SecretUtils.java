package edu.cuz.mamv2.utils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * 非对称加密工具类
 * @author VM
 * @date 2022/1/18 20:33
 * @see #RsaUtils
 */
public class SecretUtils {
    /**
     * 获取非对称密钥对
     * @param keySize 秘钥大小
     * @return 非对称密钥对
     */
    public static KeyPair getRSAKeys() {
        return getRSAKeys(1024);
    }

    /**
     * 获取非对称密钥对
     * @param keySize 秘钥大小
     * @return 非对称密钥对
     */
    public static KeyPair getRSAKeys(int keySize) {
        if (keySize == 0) {
            keySize = 1024;
        }
        KeyPair keyPair = null;
        try {
            // 指定生成器算法
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            // 初始化秘钥生成器
            keyPairGen.initialize(keySize, new SecureRandom());
            // 生成密钥对
            keyPair = keyPairGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return keyPair;
    }

    /**
     * 非对称加密操作
     * @param data 待加密数据
     * @param publickey 加密使用的公钥字符串
     * @return 加密后的数据字符串
     */
    public static String rsaEncrypt(String data, String publickey) {
        PublicKey key = getPublicKey(publickey);
        // 将数据转换为Base64格式
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 非对称解密操作
     * @param data 加密后的数据
     * @param privateKey 解密使用的私钥
     * @return 解密后的数据
     */
    public static String rsaDecrypt(String data, PrivateKey privateKey) {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.getDecoder().decode(data.getBytes());
        //RSA解密
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(cipher.doFinal(inputByte));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 将公钥字符串转换为公钥对象用于加密
     * @param key 公钥字符串
     * @return 公钥对象
     */
    public static PublicKey getPublicKey(String key) {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = null;
        PublicKey publicKey = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    /**
     * 将私钥字符串转换为私钥对象，用于解密操作
     * @param key 私钥字符串
     * @return 私钥对象
     */
    public static PrivateKey getPrivateKey(String key) {
        byte[] keyBytes;
        keyBytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = null;
        PrivateKey privateKey = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    public static String getAesKey() {
        return getAesKey(128);
    }

    public static String getAesKey(int keySize) {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(keySize);
            SecretKey secretKey = generator.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String aesEncrypt(String data, String aesKey) {
        SecretKeySpec keySpec = new SecretKeySpec(aesKey.getBytes(), "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String aesDecrypt(String data, String aesKey) {
        SecretKeySpec keySpec = new SecretKeySpec(aesKey.getBytes(), "AES");
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(data));
            return new String(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return "";
    }
}
