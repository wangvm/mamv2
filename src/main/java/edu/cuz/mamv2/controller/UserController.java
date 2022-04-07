package edu.cuz.mamv2.controller;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.cuz.mamv2.entity.User;
import edu.cuz.mamv2.entity.dto.ValidationList;
import edu.cuz.mamv2.service.UserService;
import edu.cuz.mamv2.utils.BackEnum;
import edu.cuz.mamv2.utils.BackMessage;
import edu.cuz.mamv2.utils.SecretUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
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
public class UserController {

    private final UserService userService;
    private final RedisTemplate redisTemplate;

    @GetMapping("/test")
    public BackMessage test(String value) {
        return new BackMessage(BackEnum.SUCCESS, value);
    }

    @PostMapping("/add")
    public BackMessage addUser(@RequestBody User user) {
        // 对user数据预处理
        user.setCreateTime(System.currentTimeMillis());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // 保存到数据库
        boolean ret = userService.save(user);
        if (ret) {
            return new BackMessage().successWithMessage("注册成功");
        } else {
            return new BackMessage().failureWithMessage("注册失败，请核对用户信息");
        }
    }

    @PostMapping("/addmany")
    public BackMessage addManyUser(@RequestBody ValidationList<User> users) {
        for (User user: users) {
            // 对user数据预处理
            user.setCreateTime(System.currentTimeMillis());
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
        }
        userService.saveIgnoreBatch(users);
        return new BackMessage(BackEnum.SUCCESS);
    }

    @PostMapping("/delete")
    public BackMessage deleteUser(@RequestBody User user) {
        String account = user.getAccount();
        if (account == null) {
            return new BackMessage(BackEnum.DATA_ERROR);
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("account", account);
        userService.remove(wrapper);
        return new BackMessage(BackEnum.SUCCESS);
    }

    @PostMapping("/update")
    public BackMessage updateUserInfo(@RequestBody User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getAccount, user.getAccount());
        boolean ret = userService.update(user, queryWrapper);
        // 重新加密密码，加盐值
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encryptPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptPassword);
        if (ret) {
            return new BackMessage(BackEnum.SUCCESS);
        } else {
            return new BackMessage(BackEnum.DATA_ERROR);
        }
    }

    @PostMapping("/update/username")
    public BackMessage updateUsername(@RequestBody User user) {
        String username = user.getUsername();
        String account = user.getAccount();
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        wrapper.eq("account", account).set("username", username);
        boolean ret = userService.update(wrapper);
        if (ret) {
            return new BackMessage(BackEnum.SUCCESS);
        } else {
            return new BackMessage(BackEnum.BAD_REQUEST);
        }
    }

    @PostMapping("/update/password")
    public BackMessage updatePassword(@RequestBody User user,
                                      String secretKeyId) {
        // 获取需要修改的信息
        String account = user.getAccount();
        String password = user.getPassword();

        if (ObjectUtil.isAllNotEmpty(account, password, secretKeyId)) {
            String secretKey = (String) redisTemplate.boundValueOps(secretKeyId).get();
            String decryptPasswod = SecretUtils.aesDecrypt(password, secretKey);
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encryptPassword = passwordEncoder.encode(decryptPasswod);
            boolean ret = userService.lambdaUpdate()
                    .eq(User::getAccount, account)
                    .set(User::getPassword, encryptPassword)
                    .update();
            if (ret) {
                return new BackMessage(BackEnum.SUCCESS);
            } else {
                return new BackMessage(BackEnum.BAD_REQUEST);
            }
        } else {
            return new BackMessage(BackEnum.BAD_REQUEST);
        }
    }

    /**
     * 查询指定账户的用户
     * @param account 查询参数，查询账户
     * @return 查询用户结果
     */
    @GetMapping("/query/account")
    public BackMessage queryUserByAccount(String account) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getAccount, account)
                .select(User::getAccount, User::getUsername, User::getCreateTime);
        User user = userService.getOne(queryWrapper);
        if (user == null) {
            return new BackMessage(BackEnum.DATA_ERROR);
        }
        return new BackMessage(BackEnum.SUCCESS, user);
    }

    @GetMapping("/query/name")
    public BackMessage queryUserByName(@RequestParam String username,
                                       @RequestParam(required = false, defaultValue = "0") Integer current,
                                       @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(User::getUsername, username);
        Page<User> userList = userService.page(new Page<User>(current, pageSize), queryWrapper);
        return new BackMessage(BackEnum.SUCCESS, userList);
    }

    @GetMapping("/query/cataloger")
    public BackMessage queryCatalogerByName(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getRole, "ROLE_CATALOGER")
                .like(User::getUsername, username)
                .select(User::getAccount, User::getUsername);
        List<User> userList = userService.list(queryWrapper);
        return new BackMessage(BackEnum.SUCCESS, userList);
    }

    @GetMapping("/query/auditor")
    public BackMessage queryAuditorByName(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getRole, "ROLE_AUDITOR")
                .like(User::getUsername, username)
                .select(User::getAccount, User::getUsername);
        List<User> userList = userService.list(queryWrapper);
        return new BackMessage(BackEnum.SUCCESS, userList);
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
    public BackMessage queryUserList(@RequestParam(required = false, defaultValue = "0") Integer status,
                                     @RequestParam(required = false, defaultValue = "account") String order,
                                     @RequestParam(required = false, defaultValue = "1") Integer isAsc,
                                     @RequestParam(required = false, defaultValue = "0") Integer current,
                                     @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        Page<User> users = userService.queryUserList(status, order, isAsc, current, pageSize);
        return new BackMessage(BackEnum.SUCCESS, users);
    }
}

