package edu.cuz.mamv2.config;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.cuz.mamv2.entity.MamUser;
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
                    R<Object> message = R.error("???????????????");
                    writer.write(JSONObject.toJSONString(message));
                    writer.flush();
                    writer.close();
                }))
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setContentType("application/json; charset=UTF-8");
                    PrintWriter out = response.getWriter();
                    R backMessage = R.error("????????????");
                    out.write(JSONObject.toJSONString(backMessage));
                    out.flush();
                    out.close();
                })
                .and().logout().logoutUrl("/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setContentType("application/json; charset=UTF-8");
                    PrintWriter out = response.getWriter();
                    R backMessage = R.success("??????????????????");
                    out.write(JSONObject.toJSONString(backMessage));
                    out.flush();
                    out.close();
                });

        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // ??????????????????????????????????????????
        // 1.???????????????userDetailService??????
        // 2.????????????????????????security??????
        auth.authenticationProvider(selfAuthenticationProvider);
    }

    @Bean
    LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter();
        loginFilter.setUsernameParameter("account");
        // ?????????????????????handler
        loginFilter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                String account = (String) authentication.getPrincipal();
                LambdaQueryWrapper<MamUser> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.select(MamUser::getUsername, MamUser::getAccount, MamUser::getRole).eq(MamUser::getAccount, account);
                MamUser mamUser = userMapper.selectOne(queryWrapper);
                mamUser.setPassword("");
                response.setContentType("application/json; charset=UTF-8");
                R backMessage = R.success("????????????", mamUser);
                PrintWriter out = response.getWriter();
                out.write(JSONObject.toJSONString(backMessage));
                out.flush();
                out.close();
            }
        });
        // ?????????????????????handler
        loginFilter.setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                response.setContentType("application/json; charset=UTF-8");
                PrintWriter out = response.getWriter();
                R<Object> backMessage = R.error("????????????");
                out.write(JSONObject.toJSONString(backMessage));
                out.flush();
                out.close();
            }
        });
        // ?????????????????????handler
        loginFilter.setAuthenticationManager(authenticationManagerBean());
        return loginFilter;
    }
}
