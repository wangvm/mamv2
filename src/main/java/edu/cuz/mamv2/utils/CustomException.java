package edu.cuz.mamv2.utils;

/**
 * @author VM
 * Date 2021/6/5 14:32
 * Description:
 */
public class CustomException extends RuntimeException {
    private final Integer code;

    public CustomException(HttpStatus status, String message) {
        super(message);
        this.code = status.getCode();
    }

    public Integer getCode() {
        return code;
    }
}
