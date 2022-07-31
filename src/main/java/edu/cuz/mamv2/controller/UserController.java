package edu.cuz.mamv2.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.cuz.mamv2.entity.SysUser;
import edu.cuz.mamv2.entity.dto.ValidationList;
import edu.cuz.mamv2.service.UserService;
import edu.cuz.mamv2.utils.R;
import edu.cuz.mamv2.utils.SecretUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 媒资用户表 前端控制器
 * </p>
 * @author VM
 * @since 2022/01/17 10:56
 */
@Slf4j(topic = "用户业务控制器")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;
    private final RedisTemplate redisTemplate;

    @PostMapping("/add")
    public R addUser(@RequestBody SysUser sysUser) {
        // 对user数据预处理
        sysUser.setCreateTime(System.currentTimeMillis());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(sysUser.getPassword());
        sysUser.setPassword(encodedPassword);

        // 保存到数据库
        boolean ret = userService.save(sysUser);
        if (ret) {
            return R.success("注册成功");
        } else {
            return R.error("注册失败，请核对用户信息");
        }
    }

    @PostMapping("/addmany")
    public R addManyUser(@RequestBody ValidationList<SysUser> sysUsers) {
        for (SysUser sysUser: sysUsers) {
            // 对user数据预处理
            sysUser.setCreateTime(System.currentTimeMillis());
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(sysUser.getPassword());
            sysUser.setPassword(encodedPassword);
        }
        userService.saveIgnoreBatch(sysUsers);
        return R.success();
    }

    @PostMapping("/delete")
    public R deleteUser(@RequestBody SysUser sysUser) {
        String account = sysUser.getAccount();
        if (account == null) {
            return R.error("用户不存在");
        }
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("account", account);
        userService.remove(wrapper);
        return R.success();
    }

    @PostMapping("/update")
    public R updateUserInfo(@RequestBody SysUser sysUser) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount, sysUser.getAccount());
        boolean ret = userService.update(sysUser, queryWrapper);
        // 重新加密密码，加盐值
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encryptPassword = passwordEncoder.encode(sysUser.getPassword());
        sysUser.setPassword(encryptPassword);
        return R.success();
    }

    @PostMapping("/update/username")
    public R updateUsername(@RequestBody SysUser sysUser) {
        String username = sysUser.getUsername();
        String account = sysUser.getAccount();
        UpdateWrapper<SysUser> wrapper = new UpdateWrapper<>();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        wrapper.eq("account", account).set("username", username);
        boolean ret = userService.update(wrapper);
        return R.success();
    }

    @PostMapping("/update/password")
    public R updatePassword(@RequestBody SysUser sysUser,
                            String secretKeyId) {
        // 获取需要修改的信息
        String account = sysUser.getAccount();
        String password = sysUser.getPassword();

        String secretKey = (String) redisTemplate.boundValueOps(secretKeyId).get();
        String decryptPasswod = SecretUtils.aesDecrypt(password, secretKey);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encryptPassword = passwordEncoder.encode(decryptPasswod);
        boolean ret = userService.lambdaUpdate()
                .eq(SysUser::getAccount, account)
                .set(SysUser::getPassword, encryptPassword)
                .update();
        return R.success();

    }

    /**
     * 查询指定账户的用户
     * @param account 查询参数，查询账户
     * @return 查询用户结果
     */
    @GetMapping("/query/account")
    public R queryUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount, account)
                .select(SysUser::getAccount, SysUser::getUsername, SysUser::getCreateTime);
        SysUser sysUser = userService.getOne(queryWrapper);
        return R.success(sysUser);
    }

    @GetMapping("/query/name")
    public R queryUserByName(@RequestParam String username,
                             @RequestParam(required = false, defaultValue = "0") Integer current,
                             @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(SysUser::getUsername, username);
        Page<SysUser> userList = userService.page(new Page<SysUser>(current, pageSize), queryWrapper);
        return R.success(userList);
    }

    @GetMapping("/query/cataloger")
    public R queryCatalogerByName(String username) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getRole, "ROLE_CATALOGER")
                .like(SysUser::getUsername, username)
                .select(SysUser::getAccount, SysUser::getUsername);
        List<SysUser> sysUserList = userService.list(queryWrapper);
        return R.success(sysUserList);
    }

    @GetMapping("/query/auditor")
    public R queryAuditorByName(String username) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getRole, "ROLE_AUDITOR")
                .like(SysUser::getUsername, username)
                .select(SysUser::getAccount, SysUser::getUsername);
        List<SysUser> sysUserList = userService.list(queryWrapper);
        return R.success(sysUserList);
    }

    /**
     * 根据指定条件查询用户列表
     * @param status 用户状态，null：可用账户，1：禁用账户，0：禁用账户
     * @param order 查询结果排序，默认根据account排序
     * @param isAsc 排序方式，默认为desc降序
     * @param current 分页数
     * @param pagesize 分页大小
     * @return 分页后的查询结果
     */
    @GetMapping("/query/list")
    public R queryUserList(@RequestParam(required = false, defaultValue = "0") Integer status,
                           @RequestParam(required = false, defaultValue = "account") String order,
                           @RequestParam(required = false, defaultValue = "1") Integer isAsc,
                           @RequestParam(required = false, defaultValue = "0") Integer current,
                           @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        Page<SysUser> users = userService.queryUserList(status, order, isAsc, current, pageSize);
        return R.success(users);
    }
}

