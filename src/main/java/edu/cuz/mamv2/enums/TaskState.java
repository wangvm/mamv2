package edu.cuz.mamv2.enums;

/**
 * @author VM
 * @date 2022/1/28 23:38
 * @description 用于实现任务状态码和状态的转换
 */
public enum TaskState {
    /**
     * 编目状态码
     */
    CATALOG(0, "编目中"),
    /**
     * 审核状态码
     */
    PADDING(1, "审核中"),
    /**
     * 编目完成状态码
     */
    FINISHED(2, "完成");
    private Integer code;
    private String state;

    TaskState(Integer code, String state) {
        this.code = code;
        this.state = state;
    }

    public Integer getCode() {
        return code;
    }

    public String getState() {
        return state;
    }
}
