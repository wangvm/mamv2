package edu.cuz.mamv2.handler;

import edu.cuz.mamv2.utils.BackEnum;
import edu.cuz.mamv2.utils.BackMessage;
import edu.cuz.mamv2.utils.CustomException;
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
    public BackMessage exceptionHandle(Exception e) {

        if (e instanceof CustomException) {
            CustomException customException = (CustomException) e;
            log.info("运行时异常:{}", customException.getMessage());
            return new BackMessage(customException.getCode(), customException.getMessage());
        } else {
            log.info("系统异常:{}", e.getMessage());
            e.printStackTrace();
            return new BackMessage(500, e.getMessage());
        }
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public BackMessage resolveRequestMethodNotSupportedException(Exception e) {
        log.info("请求方式异常:{}", e.getMessage());
        return new BackMessage(BackEnum.REQUEST_METHOD_ERROR);
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler({MissingServletRequestParameterException.class, HttpMessageNotReadableException.class})
    public BackMessage resolveMissingRequestParameterException(Exception e) {
        log.info("请求参数缺失异常:{}", e.getMessage());
        return new BackMessage(BackEnum.BAD_REQUEST);
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(MissingRequestHeaderException.class)
    public BackMessage resolveMissingRequestHeaderException(Exception e) {
        log.info("请求头缺失异常:{}", e.getMessage());
        return new BackMessage(401, e.getMessage());
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(AccessDeniedException.class)
    public BackMessage resolveAccessDeniedException(Exception e){
        log.info(e.getMessage());
        return new BackMessage(BackEnum.FORBIDDEN);
    }
}
