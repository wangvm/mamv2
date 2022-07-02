package edu.cuz.mamv2.utils;

/**
 * @author VM
 * Date 2021/9/18 11:04
 * Description:
 */
public enum HttpStatus {
    /**
     * 登录验证失败
     */
    LOGIN_FAILED(3),
    /**
     * 用户不存在
     */
    USER_NOT_EXISTS(4),
    /**
     * 请求正在处理中
     */
    CONTINUE(100),
    /**
     * 请求成功
     */
    SUCCESS(200),
    /**
     * 请求成功，但是没有返回值
     */
    NO_CONTENT(204),
    /**
     * 请求的资源永久重定向
     */
    MOVED_PERMANENTLY(301),
    /**
     * 请求资源重定向
     */
    FOUND(302),
    /**
     * 前端发送的请求格式不正确，或存在语法错误
     */
    BAD_REQUEST(400),
    /**
     * 未认证，或认证失败
     */
    UNAUTHORIZED(401),
    /**
     * 传入参数条目不存在或错误
     */
    DATA_ERROR(402),

    /**
     * 无权限访问资源
     */
    FORBIDDEN(403),

    /**
     * 请求服务不存在
     */
    NOT_FOUND(404),

    /**
     * 请求方式错误
     */
    REQUEST_METHOD_ERROR(405),

    /**
     * 服务器正在执行请求时发生错误
     */
    UNKNOWN_ERROR(500),

    /**
     * io错误
     */
    IO_ERROR(501),

    /**
     * 服务器暂时处于超负载或正在进行停机维护，现在无法处理请求
     */
    SERVICE_UNAVAILABLE(503);


    private Integer code;

    HttpStatus(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
