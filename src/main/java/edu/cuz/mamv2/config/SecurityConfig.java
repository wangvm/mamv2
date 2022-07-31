package edu.cuz.mamv2.config;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.cuz.mamv2.entity.SysUser;
import edu.cuz.mamv2.filter.LoginFilter;
import edu.cuz.mamv2.mapper.UserMapper;
import edu.cuz.mamv2.provider.SelfAuthenticationProvider;
import edu.cuz.mamv2.utils.R;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

        http.authorizeRequests().anyRequest().authenticated().and()
                .exceptionHandling()
                .accessDeniedHandler(((request, response, accessDeniedException) -> {
                    response.setContentType("application/json; charset=UTF-8");
                    PrintWriter writer = response.getWriter();
                    R<Object> message = R.error("无权限访问");
                    writer.write(JSONObject.toJSONString(message));
                    writer.flush();
                    writer.close();
                }))
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setContentType("application/json; charset=UTF-8");
                    PrintWriter out = response.getWriter();
                    R backMessage = R.error("认证失败");
                    out.write(JSONObject.toJSONString(backMessage));
                    out.flush();
                    out.close();
                })
                .and().logout().logoutUrl("/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setContentType("application/json; charset=UTF-8");
                    PrintWriter out = response.getWriter();
                    R backMessage = R.success("退出登录成功");
                    out.write(JSONObject.toJSONString(backMessage));
                    out.flush();
                    out.close();
                });

        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 配置通过数据进行登录授权操作
        // 1.自定义一个userDetailService组件
        // 2.将这个组件注册到security中去
        auth.authenticationProvider(selfAuthenticationProvider);
    }

    @Bean
    LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter();
        loginFilter.setUsernameParameter("account");
        // 重写成功请求的handler
        loginFilter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                String account = (String) authentication.getPrincipal();
                LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(SysUser::getUsername, SysUser::getAccount, SysUser::getRole).eq(SysUser::getAccount, account);
                SysUser sysUser = userMapper.selectOne(queryWrapper);
                sysUser.setPassword("");
                response.setContentType("application/json; charset=UTF-8");
                R backMessage = R.success("登录成功", sysUser);
                PrintWriter out = response.getWriter();
                out.write(JSONObject.toJSONString(backMessage));
                out.flush();
                out.close();
            }
        });
        // 重写失败请求的handler
        loginFilter.setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                response.setContentType("application/json; charset=UTF-8");
                PrintWriter out = response.getWriter();
                R<Object> backMessage = R.error("登录失败");
                out.write(JSONObject.toJSONString(backMessage));
                out.flush();
                out.close();
            }
        });
        // 重写验证请求的handler
        loginFilter.setAuthenticationManager(authenticationManagerBean());
        return loginFilter;
    }
}
