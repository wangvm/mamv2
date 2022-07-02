package edu.cuz.mamv2.handler;

import edu.cuz.mamv2.utils.CustomException;
import edu.cuz.mamv2.utils.HttpStatus;
import edu.cuz.mamv2.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author VM
 * Date 2022/1/10 23:00
 * Description:
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandler {
    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    public R exceptionHandle(Exception e) {

        if (e instanceof CustomException) {
            CustomException customException = (CustomException) e;
            log.info("运行时异常:{}", customException.getMessage());
            return R.error(HttpStatus.BAD_REQUEST, e.getMessage());
        } else {
            log.info("系统异常:{}", e.getMessage());
            e.printStackTrace();
            return R.error(HttpStatus.UNKNOWN_ERROR, e.getMessage());
        }
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R resolveRequestMethodNotSupportedException(Exception e) {
        log.info("请求方式异常:{}", e.getMessage());
        return R.error(HttpStatus.REQUEST_METHOD_ERROR, "请求方式异常");
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler({MissingServletRequestParameterException.class, HttpMessageNotReadableException.class})
    public R resolveMissingRequestParameterException(Exception e) {
        log.info("请求参数缺失异常:{}", e.getMessage());
        return R.error(HttpStatus.BAD_REQUEST, "参数异常");
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(MissingRequestHeaderException.class)
    public R resolveMissingRequestHeaderException(Exception e) {
        log.info("请求头缺失异常:{}", e.getMessage());
        return R.error(e.getMessage());
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(AccessDeniedException.class)
    public R resolveAccessDeniedException(Exception e) {
        log.info(e.getMessage());
        return R.error(HttpStatus.UNAUTHORIZED, "无权限访问");
    }
}
