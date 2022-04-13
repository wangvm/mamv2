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
    CATALOG("编目中"),
    /**
     * 待修改
     */
    MODEIFY("待修改"),
    /**
     * 审核状态码
     */
    PADDING("审核中"),
    /**
     * 编目完成状态码
     */
    FINISHED("完成");
    private String state;

    TaskState(String state) {
        this.state = state;
    }


    public String getState() {
        return state;
    }
}
