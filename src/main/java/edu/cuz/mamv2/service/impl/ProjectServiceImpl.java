package edu.cuz.mamv2.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.cuz.mamv2.entity.SysProject;
import edu.cuz.mamv2.mapper.ProjectMapper;
import edu.cuz.mamv2.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 媒资项目表 服务实现类
 * </p>
 * @author VM
 * @since 2022/01/17 10:56
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, SysProject> implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public Page<SysProject> queryProjectListByUser(String account, Integer current, Integer pageSize) {
        Page<Object> page = new Page<>(current, pageSize);
        Page<SysProject> projects = projectMapper.selectUserProjectsPage(page, account);
        return projects;
    }
}
