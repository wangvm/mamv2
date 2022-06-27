package edu.cuz.mamv2.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.cuz.mamv2.entity.MamTask;
import edu.cuz.mamv2.utils.BackMessage;

/**
 * <p>
 * 媒资任务表 服务类
 * </p>
 *
 * @author VM
 * @since 2022/01/17 10:56
 */
public interface TaskService extends IService<MamTask> {

    /**
     * 查询账户
     * @param account 账户
     * @param projectId 项目id
     * @param current 当前
     * @param pageSize 页面大小
     * @return {@link BackMessage}
     */
    Page<MamTask> queryByAccount(String account, Integer projectId, Integer current, Integer pageSize);
}
