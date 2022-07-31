package edu.cuz.mamv2.entity;

import lombok.Data;

/**
 * @author VM
 * @date 2022/4/9 14:58
 */
@Data
public class TaskDTO extends SysTask {
    private VideoInfo videoInfo;
}
