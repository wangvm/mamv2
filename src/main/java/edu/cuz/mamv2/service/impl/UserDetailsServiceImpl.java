package edu.cuz.mamv2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.cuz.mamv2.entity.User;
import edu.cuz.mamv2.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author VM
 * Date 2022/1/14 14:45
 * Description:自定义数据库查询登录实现，用于security中替换默认登录授权实现
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username)
                .select("username", "password");
        User user = userMapper.selectOne(wrapper);
        return null;
    }
}
