package edu.cuz.mamv2.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 媒资任务表
 * </p>
 * @author VM
 * @since 2022/01/17 10:56
 */
@Getter
@Setter
@TableName("mam_task")
public class SysTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 任务名称
     */
    @TableField(insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String name;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 归属项目
     */
    private Long project;
    /**
     * 归属项目名称
     */
    private String projectName;
    /**
     * 编目员
     */
    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private SysUser cataloger;
    /**
     * 审核员
     */
    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private SysUser auditor;
    /**
     * 编目员 id
     */
    private Long catalogerId;
    /**
     * 审核员 id
     */
    private Long auditorId;
    /**
     * 任务状态，0：编目中、1：待修改、2：审核中、3：完成
     */
    private String status;
    /**
     * 视频信息id
     */
    private String videoInfoId;
}
