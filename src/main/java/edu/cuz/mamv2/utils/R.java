package edu.cuz.mamv2.utils;

/**
 * @author VM
 * Date 2021/9/18 10:52
 * Description: 请求响应体
 */
public class R<T> {
    private Integer code;

    private String message;

    private T data;

    private R() {
    }

    private R(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private R(HttpStatus status, String message) {
        this.code = status.getCode();
        this.message = message;
    }

    private R(HttpStatus status, String message, T data) {
        this.code = status.getCode();
        this.message = message;
        this.data = data;
    }

    public static R success() {
        return new R(HttpStatus.SUCCESS, "成功");
    }

    public static <T> R success(T data) {
        return new R(HttpStatus.SUCCESS, "成功", data);
    }

    public static R success(String message) {
        return new R(HttpStatus.SUCCESS, message);
    }

    public static <T> R success(String message, T data) {
        return new R(HttpStatus.SUCCESS, message, data);
    }

    public static R error(String message) {
        return new R(HttpStatus.BAD_REQUEST, message);
    }

    public static R error(Integer code, String message) {
        return new R(code, message);
    }

    public static R error(HttpStatus status, String message) {
        return new R(status, message);
    }

    public static <T> R error(String message, T data) {
        return new R(HttpStatus.BAD_REQUEST, message, data);
    }

    public static R toResult(int ret) {
        return ret > 0 ? success() : error("失败");
    }

    public static R toResult(int[] ret) {
        if (ret.length == 0) {
            return error("失败");
        }
        return success();
    }

    public static R toResult(Object object) {
        if (object == null) {
            return error("失败");
        }
        return success();
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
