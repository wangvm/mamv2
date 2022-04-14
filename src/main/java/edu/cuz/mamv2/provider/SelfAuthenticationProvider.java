package edu.cuz.mamv2.provider;

import edu.cuz.mamv2.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author VM
 * @date 2022/2/19 20:06
 * @description
 */
@Component
public class SelfAuthenticationProvider implements AuthenticationProvider {
    @Resource
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String account = authentication.getName();
        String password = (String) authentication.getCredentials();
        // 在用户 service 中实现 @UserDetailsService#loadUserByUsername 方法
        // 在其中使用数据查询获取用户信息
        UserDetails userDetails = userService.loadUserByUsername(account);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean checkPassword = bCryptPasswordEncoder.matches(password, userDetails.getPassword());
        if (!checkPassword) {
            throw new BadCredentialsException("账户或密码不正确，请重试");
        }
        return new UsernamePasswordAuthenticationToken(account, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
