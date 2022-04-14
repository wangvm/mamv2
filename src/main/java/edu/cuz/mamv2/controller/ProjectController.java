package edu.cuz.mamv2.controller;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.cuz.mamv2.entity.Project;
import edu.cuz.mamv2.service.ProjectService;
import edu.cuz.mamv2.utils.BackEnum;
import edu.cuz.mamv2.utils.BackMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 媒资项目表 前端控制器
 * </p>
 * @author VM
 * @since 2022/01/17 10:56
 */
@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public BackMessage addProject(@RequestBody Project project) {
        // 预处理，添加项目创建时间
        project.setCreateTime(System.currentTimeMillis());
        // 执行保存操作
        boolean ret;
        try {
            ret = projectService.save(project);
        } catch (DuplicateKeyException e) {
            return new BackMessage(BackEnum.DATA_ERROR.getCode(), "项目已存在");
        }
        if (ret) {
            return new BackMessage(BackEnum.SUCCESS);
        } else {
            return new BackMessage(BackEnum.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete")
    public BackMessage deleteProject(@RequestBody Project project) {
        // 提取删除所需的数据：项目名称和id
        Long id = project.getId();
        boolean ret = projectService.removeById(id);
        if (ret) {
            return new BackMessage(BackEnum.SUCCESS);
        } else {
            return new BackMessage(BackEnum.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update/leader")
    public BackMessage updateLeader(@RequestBody Project project) {
        // 提取删除所需的数据：项目名称和id
        Long id = project.getId();
        Long leader = project.getLeader();
        String leaderName = project.getLeaderName();
        boolean ret = projectService.lambdaUpdate()
                .set(Project::getLeader, leader)
                .set(Project::getLeaderName, leaderName)
                .eq(Project::getId, id).update();
        if (ret) {
            return new BackMessage(BackEnum.SUCCESS);
        } else {
            return new BackMessage(BackEnum.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update/name")
    public BackMessage updateProjectName(@RequestBody Project project) {
        Long id = project.getId();
        String name = project.getName();
        boolean ret = projectService.lambdaUpdate()
                .set(Project::getName, name)
                .eq(Project::getId, id).update();
        if (ret) {
            return new BackMessage(BackEnum.SUCCESS);
        } else {
            return new BackMessage(BackEnum.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/task/add")
    public BackMessage updateTaskNumber(Integer projectId) {
        boolean ret = projectService.lambdaUpdate()
                .setSql("taskCount = taskCount+1")
                .eq(Project::getId, projectId)
                .update();
        if (ret) {
            return new BackMessage(BackEnum.SUCCESS);
        } else {
            return new BackMessage(BackEnum.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/task/finished")
    public BackMessage finishedTask(Integer projectId) {
        boolean ret = projectService.lambdaUpdate()
                .setSql("finishedTask = finishedTask+1")
                .eq(Project::getId, projectId)
                .update();
        if (ret) {
            return new BackMessage(BackEnum.SUCCESS);
        } else {
            return new BackMessage(BackEnum.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/query/admin")
    public BackMessage queryProjectList(@RequestParam(required = false, defaultValue = "0") Integer status,
                                        @RequestParam(required = false, defaultValue = "account") String order,
                                        @RequestParam(required = false, defaultValue = "1") Integer isAsc,
                                        @RequestParam(required = false, defaultValue = "0") Integer current,
                                        @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        QueryWrapper<Project> queryWrapper = new QueryWrapper<Project>();
        queryWrapper.eq("deleted", status)
                .orderBy(true, isAsc > 0 ? true : false, order);
        Page<Project> page = projectService.page(new Page<Project>(current, pageSize),
                queryWrapper);
        return new BackMessage(BackEnum.SUCCESS, page);
    }

    @PreAuthorize("hasAnyRole('CATALOGER','AUDITOR')")
    @GetMapping("/query/user")
    public BackMessage queryProjectListByUser(String account,
                                              @RequestParam(required = false, defaultValue = "0") Integer current,
                                              @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        Page<Project> projects = projectService.queryProjectListByUser(account, current, pageSize);
        long ret = projects.getSize();
        if (ret > 0) {
            return new BackMessage(BackEnum.SUCCESS, projects);
        } else {
            return new BackMessage(BackEnum.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/query/name")
    public BackMessage queryProjectByName(String name) {
        LambdaQueryWrapper<Project> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Project::getName, name);
        Project project = projectService.getOne(queryWrapper);
        if (ObjectUtil.isNotEmpty(project)) {
            return new BackMessage(BackEnum.SUCCESS, project);
        } else {
            return new BackMessage(BackEnum.BAD_REQUEST);
        }
    }
}

