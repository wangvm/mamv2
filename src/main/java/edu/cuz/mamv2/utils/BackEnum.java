package edu.cuz.mamv2.utils;

/**
 * @author VM
 * Date 2021/9/18 11:04
 * Description:
 */
public enum BackEnum {
    /**
     * 登录验证失败
     */
    LOGIN_FAILED(3, "账户或密码错误"),
    /**
     * 用户不存在
     */
    USER_NOT_EXISTS(4, "用户不存在"),
    /**
     * 请求正在处理中
     */
    CONTINUE(100, "继续发送请求或者忽略这个响应"),
    /**
     * 请求成功
     */
    SUCCESS(200, "成功"),
    /**
     * 请求成功，但是没有返回值
     */
    NO_CONTENT(204, "请求成功"),
    /**
     * 请求的资源永久重定向
     */
    MOVED_PERMANENTLY(301, "请求资源永久重定向"),
    /**
     * 请求资源重定向
     */
    FOUND(302, "资源重定向"),
    /**
     * 前端发送的请求格式不正确，或存在语法错误
     */
    BAD_REQUEST(400, "参数格式错误"),
    /**
     * 未认证，或认证失败
     */
    UNAUTHORIZED(401, "请先登录"),
    /**
     * 传入参数条目不存在或错误
     */
    DATA_ERROR(402, "数据错误，数据不存在或者重复"),

    /**
     * 无权限访问资源
     */
    FORBIDDEN(403, "无权限访问"),

    /**
     * 请求服务不存在
     */
    NOT_FOUND(404, "请求不存在"),

    /**
     * 请求方式错误
     */
    REQUEST_METHOD_ERROR(405, "请求方式错误"),

    /**
     * 服务器正在执行请求时发生错误
     */
    UNKNOWN_ERROR(500, "服务未知错误"),

    /**
     * io错误
     */
    IO_ERROR(501, "服务IO错误，请重试"),

    /**
     * 服务器暂时处于超负载或正在进行停机维护，现在无法处理请求
     */
    SERVICE_UNAVAILABLE(503, "服务暂停服务");


    private Integer code;
    private String message;

    BackEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
