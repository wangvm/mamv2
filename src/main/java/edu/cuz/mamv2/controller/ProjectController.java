package edu.cuz.mamv2.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.cuz.mamv2.entity.SysProject;
import edu.cuz.mamv2.service.ProjectService;
import edu.cuz.mamv2.utils.R;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
    public R addProject(@RequestBody SysProject sysProject) {
        // 预处理，添加项目创建时间
        sysProject.setCreateTime(System.currentTimeMillis());
        // 执行保存操作
        try {
            projectService.save(sysProject);
        } catch (DuplicateKeyException e) {
            return R.error("项目已存在");
        }
        return R.success("添加项目成功");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete")
    public R deleteProject(@RequestBody SysProject sysProject) {
        // 提取删除所需的数据：项目名称和id
        Long id = sysProject.getId();
        boolean ret = projectService.removeById(id);
        if (!ret) {
            return R.error("项目不存在");
        }
        return R.success("删除项目成功");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update/leader")
    public R updateLeader(@Validated(Update.class) @RequestBody SysProject sysProject) {
        // 提取所需的数据：项目名称和id
        Long id = sysProject.getId();
        Long leader = sysProject.getLeader();
        String leaderName = sysProject.getLeaderName();
        projectService.lambdaUpdate()
                .set(SysProject::getLeader, leader)
                .set(SysProject::getLeaderName, leaderName)
                .eq(SysProject::getId, id).update();
        return R.success("更新成功");

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update/name")
    public R updateProjectName(@Validated(Update.class) @RequestBody SysProject sysProject) {
        Long id = sysProject.getId();
        String name = sysProject.getName();
        projectService.lambdaUpdate()
                .set(SysProject::getName, name)
                .eq(SysProject::getId, id).update();
        return R.success("更新成功");

    }

    @Deprecated
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/task/add")
    public R updateTaskNumber(Integer projectId) {
        boolean ret = projectService.lambdaUpdate()
                .setSql("taskCount = taskCount+1")
                .eq(SysProject::getId, projectId)
                .update();
        return R.success();
    }

    @Deprecated
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/task/finished")
    public R finishedTask(Integer projectId) {
        boolean ret = projectService.lambdaUpdate()
                .setSql("finishedTask = finishedTask+1")
                .eq(SysProject::getId, projectId)
                .update();
        return R.success();

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/query/admin")
    public R queryProjectList(@RequestParam(required = false, defaultValue = "0") Integer status,
                              @RequestParam(required = false, defaultValue = "account") String order,
                              @RequestParam(required = false, defaultValue = "1") Integer isAsc,
                              @RequestParam(required = false, defaultValue = "0") Integer current,
                              @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        QueryWrapper<SysProject> queryWrapper = new QueryWrapper<SysProject>();
        queryWrapper.eq("deleted", status)
                .orderBy(true, isAsc > 0 ? true : false, order);
        Page<SysProject> page = projectService.page(new Page<SysProject>(current, pageSize),
                queryWrapper);
        return R.success(page);
    }

    @PreAuthorize("hasAnyRole('CATALOGER','AUDITOR')")
    @GetMapping("/query/user")
    public R queryProjectListByUser(String account,
                                    @RequestParam(required = false, defaultValue = "0") Integer current,
                                    @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        Page<SysProject> projects = projectService.queryProjectListByUser(account, current, pageSize);
        long ret = projects.getSize();
        return R.success(projects);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/query/name")
    public R queryProjectByName(String name) {
        LambdaQueryWrapper<SysProject> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysProject::getName, name);
        SysProject sysProject = projectService.getOne(queryWrapper);
        return R.success(sysProject);
    }
}

