package edu.cuz.mamv2.utils;

/**
 * @author VM
 * Date 2021/9/18 10:52
 * Description: 请求响应体
 */
public class BackMessage<T> {
    private Integer code;

    private String message;

    private T data;

    public BackMessage() {
    }

    public BackMessage(BackEnum backEnum) {
        this.code = backEnum.getCode();
        this.message = backEnum.getMessage();
    }

    public BackMessage(BackEnum backEnum, T data) {
        this.code = backEnum.getCode();
        this.message = backEnum.getMessage();
        this.data = data;
    }

    public BackMessage(Integer code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }

    public BackMessage successWithMessage(String message) {
        this.code = 200;
        this.message = message;
        this.data = null;
        return this;
    }

    public BackMessage successWithMessageAndData(String message, T data) {
        this.code = 200;
        this.message = message;
        this.data = data;
        return this;
    }

    public BackMessage failureWithMessage(String message) {
        this.code = 400;
        this.message = message;
        this.data = null;
        return this;
    }

    public BackMessage failureWithCodeAndMessage(Integer code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
