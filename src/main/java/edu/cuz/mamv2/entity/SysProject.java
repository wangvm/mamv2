package edu.cuz.mamv2.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class SysProject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 项目id
     */
    @NotNull(groups = {Update.class})
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
    @TableField(updateStrategy = FieldStrategy.NOT_NULL, insertStrategy = FieldStrategy.NOT_NULL)
    private Long leader;

    /**
     * 负责人名称
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY, insertStrategy = FieldStrategy.NOT_EMPTY)
    private String leaderName;

    /**
     * 项目任务数量
     */
    private Integer taskCount;

    /**
     * 已完成任务数量
     */
    private Integer finishedTask;
}
