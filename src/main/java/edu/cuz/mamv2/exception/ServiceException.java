package edu.cuz.mamv2.exception;

/**
 * @author VM
 * @date 2022/7/30 20:56
 */
public class ServiceException extends RuntimeException {
    private Integer code;

    public ServiceException(String message) {
        super(message);
        this.code = 400;
    }

    public ServiceException(String message, Integer code) {
        super(message);
        this.code = code;
    }


    public Integer getCode() {
        return code;
    }
}
