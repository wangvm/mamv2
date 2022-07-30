package edu.cuz.mamv2.aop;

import edu.cuz.mamv2.exception.ServiceException;
import edu.cuz.mamv2.utils.HttpStatus;
import edu.cuz.mamv2.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

/**
 * @author VM
 * Date 2022/1/10 23:00
 * Description:
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = ServiceException.class)
    public R serviceExceptionHandler(ServiceException e) {
        log.error("运行时异常:{}", e.getMessage());
        return R.error(e.getCode(), e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R resolveRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("请求方式异常:{}", e.getMessage());
        return R.error(HttpStatus.REQUEST_METHOD_ERROR, "请求方式异常");
    }

    @ResponseBody
    @ExceptionHandler({MissingServletRequestParameterException.class, HttpMessageNotReadableException.class})
    public R resolveMissingRequestParameterException(Exception e) {
        log.error("请求参数缺失异常:{}", e.getMessage());
        return R.error(HttpStatus.BAD_REQUEST, "参数异常");
    }

    @ResponseBody
    @ExceptionHandler(MissingRequestHeaderException.class)
    public R resolveMissingRequestHeaderException(MissingRequestHeaderException e) {
        log.error("请求头缺失异常:{}", e.getMessage());
        return R.error(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(AccessDeniedException.class)
    public R resolveAccessDeniedException(AccessDeniedException e) {
        log.error(e.getMessage());
        return R.error(HttpStatus.UNAUTHORIZED, "无权限访问");
    }

    @ResponseBody
    @ExceptionHandler(value = NoSuchElementException.class)
    public R noSuchElementExceptionHandler(NoSuchElementException e){
        log.info("数据不存在：{}", e.getMessage());
        return R.error("数据不存在");
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public R exceptionHandler(Exception e) {
        log.error("系统异常:{}", e.getMessage());
        return R.error(HttpStatus.UNKNOWN_ERROR, e.getMessage());
    }

}
