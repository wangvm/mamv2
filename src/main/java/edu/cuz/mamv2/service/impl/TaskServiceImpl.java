package edu.cuz.mamv2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.cuz.mamv2.entity.MamTask;
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
public class TaskServiceImpl extends ServiceImpl<TaskMapper, MamTask> implements TaskService {
    @Resource
    private TaskMapper taskMapper;

    @Override
    public Page<MamTask> queryByAccount(String account, Integer projectId, Integer current, Integer pageSize) {
        LambdaQueryWrapper<MamTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MamTask::getProject, projectId)
                .and(i -> i.eq(MamTask::getCataloger, account).or().eq(MamTask::getAuditor, account));
        Page<MamTask> page = taskMapper.selectPage(Page.of(current, pageSize), wrapper);
        return page;
    }
}
