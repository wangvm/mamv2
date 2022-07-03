package edu.cuz.mamv2.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 媒资用户表
 * </p>
 * @author VM
 * @since 2022/01/17 10:56
 */
@Getter
@Setter
@TableName("mam_user")
public class MamUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户账户
     */
    private String account;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 注册时间
     */
    private Long createTime;

    /**
     * 用户权限设置，只有三种权限，ADMIN、CATALOGER、AUDITOR
     */
    private String role;
}
