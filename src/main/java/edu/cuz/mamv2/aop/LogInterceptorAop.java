package edu.cuz.mamv2.aop;

/**
 * 日志aop，处理请求开始和结束的参数日志打印
 * @author VM
 * Date 2022/1/10 22:58
 * Description:
 */

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LogInterceptorAop {
    @Pointcut("execution(* edu.cuz.mamv2.controller.*.*(..))")
    private void pointcut() {
    }

    @Around("pointcut()")
    public Object aroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 通过上下文管理器获取拿到请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        StringBuilder params = new StringBuilder();
        String method = request.getMethod();
        // 判定请求方法，取出请求内的参数
        if (HttpMethod.GET.toString().equals(method)) {
            String queryString = request.getQueryString();
            if (StringUtils.hasLength(queryString)) {
                params.append(URLDecoder.decode(queryString, "UTF-8"));
            }
        } else {
            Object[] args = proceedingJoinPoint.getArgs();
            params.append(Arrays.toString(args));
        }
        // 打印请求入参
        log.info("接受请求, url:{}, method:{}, paramter:{}", request.getRequestURL(), request.getMethod(), params);
        // 执行请求
        Object o = null;
        o = proceedingJoinPoint.proceed();
        // 请求处理以后打印出参
        log.info("请求完成, url:{}, method:{}, result:{}", request.getRequestURL(),
                request.getMethod(), JSONObject.toJSONString(o));
        return o;
    }
}
