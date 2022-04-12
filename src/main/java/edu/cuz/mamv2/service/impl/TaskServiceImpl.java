package edu.cuz.mamv2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.cuz.mamv2.entity.Task;
import edu.cuz.mamv2.mapper.TaskMapper;
import edu.cuz.mamv2.service.TaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 媒资任务表 服务实现类
 * </p>
 * @author VM
 * @since 2022/01/17 10:56
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {
    @Resource
    private TaskMapper taskMapper;

    @Override
    public Page<Task> queryByAccount(String account, Integer projectId, Integer current, Integer pageSize) {
        LambdaQueryWrapper<Task> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Task::getProject, projectId)
                .and(i -> i.eq(Task::getCataloger, account).or().eq(Task::getAuditor, account));
        Page<Task> page = taskMapper.selectPage(Page.of(current, pageSize), wrapper);
        return page;
    }
}
