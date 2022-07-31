package edu.cuz.mamv2.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.cuz.mamv2.entity.SysProject;

/**
 * <p>
 * 媒资项目表 服务类
 * </p>
 * @author VM
 * @since 2022/01/17 10:56
 */
public interface ProjectService extends IService<SysProject> {

    /**
     * 根据用户id查询用户所参与的项目列表
     * @param account 用户id
     * @param current 当前页
     * @param pageSize 分页大小
     * @return 项目列表
     */
    Page<SysProject> queryProjectListByUser(String account, Integer current, Integer pageSize);
}
