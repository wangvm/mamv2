package edu.cuz.mamv2.entity;

import edu.cuz.mamv2.entity.dto.VideoDTO;
import lombok.Data;

/**
 * @author VM
 * @date 2022/4/9 14:58
 */
@Data
public class TaskDTO {
    private MamTask mamTaskInfo;
    private VideoDTO videoInfo;
}
