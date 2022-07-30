package edu.cuz.mamv2.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.cuz.mamv2.entity.MamProject;
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
    public R addProject(@RequestBody MamProject mamProject) {
        // 预处理，添加项目创建时间
        mamProject.setCreateTime(System.currentTimeMillis());
        // 执行保存操作
        try {
            projectService.save(mamProject);
        } catch (DuplicateKeyException e) {
            return R.error("项目已存在");
        }
        return R.success("添加项目成功");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete")
    public R deleteProject(@RequestBody MamProject mamProject) {
        // 提取删除所需的数据：项目名称和id
        Long id = mamProject.getId();
        boolean ret = projectService.removeById(id);
        if (!ret) {
            return R.error("项目不存在");
        }
        return R.success("删除项目成功");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update/leader")
    public R updateLeader(@Validated(Update.class) @RequestBody MamProject mamProject) {
        // 提取所需的数据：项目名称和id
        Long id = mamProject.getId();
        Long leader = mamProject.getLeader();
        String leaderName = mamProject.getLeaderName();
        projectService.lambdaUpdate()
                .set(MamProject::getLeader, leader)
                .set(MamProject::getLeaderName, leaderName)
                .eq(MamProject::getId, id).update();
        return R.success("更新成功");

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update/name")
    public R updateProjectName(@Validated(Update.class) @RequestBody MamProject mamProject) {
        Long id = mamProject.getId();
        String name = mamProject.getName();
        projectService.lambdaUpdate()
                .set(MamProject::getName, name)
                .eq(MamProject::getId, id).update();
        return R.success("更新成功");

    }

    @Deprecated
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/task/add")
    public R updateTaskNumber(Integer projectId) {
        boolean ret = projectService.lambdaUpdate()
                .setSql("taskCount = taskCount+1")
                .eq(MamProject::getId, projectId)
                .update();
        return R.success();
    }

    @Deprecated
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/task/finished")
    public R finishedTask(Integer projectId) {
        boolean ret = projectService.lambdaUpdate()
                .setSql("finishedTask = finishedTask+1")
                .eq(MamProject::getId, projectId)
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
        QueryWrapper<MamProject> queryWrapper = new QueryWrapper<MamProject>();
        queryWrapper.eq("deleted", status)
                .orderBy(true, isAsc > 0 ? true : false, order);
        Page<MamProject> page = projectService.page(new Page<MamProject>(current, pageSize),
                queryWrapper);
        return R.success(page);
    }

    @PreAuthorize("hasAnyRole('CATALOGER','AUDITOR')")
    @GetMapping("/query/user")
    public R queryProjectListByUser(String account,
                                    @RequestParam(required = false, defaultValue = "0") Integer current,
                                    @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        Page<MamProject> projects = projectService.queryProjectListByUser(account, current, pageSize);
        long ret = projects.getSize();
        return R.success(projects);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/query/name")
    public R queryProjectByName(String name) {
        LambdaQueryWrapper<MamProject> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MamProject::getName, name);
        MamProject mamProject = projectService.getOne(queryWrapper);
        return R.success(mamProject);
    }
}

