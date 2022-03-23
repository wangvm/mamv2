package edu.cuz.mamv2.utils;

/**
 * @author VM
 * Date 2021/6/5 14:32
 * Description:
 */
public class CustomException extends RuntimeException{
    private final Integer code;

    public CustomException(BackEnum backEnum){
        super(backEnum.getMessage());
        this.code = backEnum.getCode();
    }

    public CustomException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
