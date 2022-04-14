package edu.cuz.mamv2.task;

import edu.cuz.mamv2.mapper.ProjectMapper;
import edu.cuz.mamv2.mapper.TaskMapper;
import edu.cuz.mamv2.mapper.UserMapper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * 清理数据数据的定时任务
 * @author VM
 * @date 2022/4/14 23:58
 */
@Component
@EnableScheduling
public class CleanDataTask {

    @Resource
    private ProjectMapper projectMapper;
    @Resource
    private TaskMapper taskMapper;
    @Resource
    private UserMapper userMapper;

    /**
     * 清理数据库无效数据
     * 采用cron定时方式指定
     * 表达式 0 0 23 L * ? 表示每个月的最后一天23分执行一次操作
     * 0 0 1 * * ? 表示每天1点执行一次
     */
    @Scheduled(cron = "0 0 1 * * ?")
    private void cleanDatabaseTask() {
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("deleted", 1);
        // 清除已经伪删除的数据
        projectMapper.deleteByMap(map);
        taskMapper.deleteByMap(map);
        userMapper.deleteByMap(map);
    }
}
