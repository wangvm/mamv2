package edu.cuz.mamv2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.cuz.mamv2.entity.Task;
import edu.cuz.mamv2.mapper.TaskMapper;
import edu.cuz.mamv2.service.TaskService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 媒资任务表 服务实现类
 * </p>
 *
 * @author VM
 * @since 2022/01/17 10:56
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {

}
