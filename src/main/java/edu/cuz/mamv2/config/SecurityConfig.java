package edu.cuz.mamv2.config;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.cuz.mamv2.entity.User;
import edu.cuz.mamv2.mapper.UserMapper;
import edu.cuz.mamv2.provider.SelfAuthenticationProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.annotation.Resource;
import java.io.PrintWriter;

/**
 * @author VM
 * Date 2022/1/9 0:31
 * Description:
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private SelfAuthenticationProvider selfAuthenticationProvider;
    @Resource
    private UserMapper userMapper;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();

        http.authorizeRequests()
                .and()
                .formLogin()
                .permitAll()
                .usernameParameter("account")
                .successHandler((request, response, authentication) -> {
                    String account = (String) authentication.getPrincipal();
                    LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.select(User::getUsername, User::getAccount, User::getRole)
                            .eq(User::getAccount, account);
                    User user = userMapper.selectOne(queryWrapper);
                    response.setContentType("application/json; charset=UTF-8");
                    PrintWriter out = response.getWriter();
                    out.write(JSONObject.toJSONString(user));
                    out.flush();
                    out.close();
                })
                .failureHandler((request, response, exception) -> {
                    response.setContentType("application/json; charset=UTF-8");
                    PrintWriter out = response.getWriter();
                    out.write("账号或密码错误，请重试");
                    out.flush();
                    out.close();
                })
                .and().exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setContentType("application/json; charset=UTF-8");
                    PrintWriter out = response.getWriter();
                    out.write("请先登录");
                    out.flush();
                    out.close();
                })
                .and().logout().logoutUrl("/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setContentType("application/json; charset=UTF-8");
                    PrintWriter out = response.getWriter();
                    out.write("logout success");
                    out.flush();
                    out.close();
                });
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 配置通过数据进行登录授权操作
        // 1.自定义一个userDetailService组件
        // 2.将这个组件注册到security中去
        auth.authenticationProvider(selfAuthenticationProvider);
        // auth.userDetailsService(userDetailsService);
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
