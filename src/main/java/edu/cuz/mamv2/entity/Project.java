package edu.cuz.mamv2.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 媒资项目表
 * </p>
 *
 * @author VM
 * @since 2022/01/17 10:56
 */
@Getter
@Setter
@TableName("mam_project")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 项目id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 项目负责人账户
     */
    private Long leader;

    /**
     * 负责人名称
     */
    private String leaderName;

    /**
     * 项目任务数量
     */
    private Integer taskCount;

    /**
     * 已完成任务数量
     */
    private Integer finishedTask;

    /**
     * 逻辑删除控制位
     */
    private Integer deleted;
}
