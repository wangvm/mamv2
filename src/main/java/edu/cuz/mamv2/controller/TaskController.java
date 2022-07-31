package edu.cuz.mamv2.controller;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.cuz.mamv2.entity.*;
import edu.cuz.mamv2.entity.dto.*;
import edu.cuz.mamv2.enums.TaskState;
import edu.cuz.mamv2.repository.ProgramRepository;
import edu.cuz.mamv2.service.TaskService;
import edu.cuz.mamv2.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Optional;

/**
 * <p>
 * 媒资任务表 前端控制器
 * </p>
 * @author VM
 * @since 2022/01/17 10:56
 */
@Slf4j
@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskService taskService;
    @Resource
    private ProgramRepository programRepository;
    @Resource
    private RestHighLevelClient restHighLevelClient;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public R addTask(@RequestBody TaskDTO taskDTO) {
        SysTask sysTask = taskDTO.getTaskInfo();
        VideoInfo videoInfo = taskDTO.getVideoInfo();
        sysTask.setCreateTime(System.currentTimeMillis());
        sysTask.setVideoInfoId(videoInfo.getId());
        boolean ret = taskService.save(sysTask);
        String filename = videoInfo.getFileName();
        MamProgram program = new MamProgram();
        program.setTaskId(sysTask.getId());
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setCheck(0);
        menu.setContent(filename);
        menu.setLevel("节目层");
        menu.setParent(null);
        program.setMenu(menu);
        program.setTitle(new Attributes(filename));
        program.setAspectRatio(new Attributes(videoInfo.getAspectRatio().asEncoderArgument()));
        program.setAudioChannel(new Attributes(videoInfo.getAudioChannel().toString()));
        program.setStartPoint(new Attributes("0"));
        program.setOutPoint(new Attributes(String.valueOf(videoInfo.getDuration() / 1000)));
        log.info(program.toString());
        Optional<MamProgram> b = programRepository.findByTaskId(program.getTaskId());
        if (b.isPresent()) {
            return R.error("任务已存在");
        }
        MamProgram save = programRepository.save(program);
        if (save == null) {
            return R.error("添加失败，请重试");
        }
        return R.success("添加任务成功");

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addMany")
    public R addManyTask(@RequestBody ValidationList<SysTask> sysTasks) {
        for (SysTask sysTask: sysTasks) {
            sysTask.setCreateTime(System.currentTimeMillis());
        }
        boolean ret = taskService.saveBatch(sysTasks);
        return R.success();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete")
    public R deleteTask(@RequestBody SysTask sysTask) {
        Long id = sysTask.getId();
        boolean ret = taskService.removeById(id);
        DeleteByQueryRequest request = new DeleteByQueryRequest("program", "fragment", "scenes");
        request.setQuery(QueryBuilders.termQuery("taskId", id));
        try {
            BulkByScrollResponse response = restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update/name")
    public R updateName(@RequestBody SysTask sysTask) {
        String name = sysTask.getName();
        Long id = sysTask.getId();
        LambdaUpdateWrapper<SysTask> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(SysTask::getName, name)
                .eq(SysTask::getId, id);
        boolean ret = taskService.update(updateWrapper);
        return R.success();
    }

    @Deprecated
    @PostMapping("/update/project")
    public R updateProject(@RequestBody SysTask sysTask) {
        Long name = sysTask.getProject();
        Long projectId = sysTask.getProject();
        String projectName = sysTask.getProjectName();
        LambdaUpdateWrapper<SysTask> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(SysTask::getName, name)
                .set(SysTask::getProjectName, projectName)
                .eq(SysTask::getProject, projectId);
        boolean ret = taskService.update(updateWrapper);
        return R.success();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update")
    public R updateTaskInfo(@RequestBody SysTask sysTask) {
        boolean ret = taskService.updateById(sysTask);
        return R.success("更新成功");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update/cataloger")
    public R updateCataloger(@RequestBody SysTask sysTask) {
        Long name = sysTask.getProject();
        Long cataloger = sysTask.getCataloger();
        String catalogerName = sysTask.getCatalogerName();
        LambdaUpdateWrapper<SysTask> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(SysTask::getName, name)
                .set(SysTask::getProjectName, catalogerName)
                .eq(SysTask::getProject, cataloger);
        boolean ret = taskService.update(updateWrapper);
        return R.success();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update/auditor")
    public R updateAuditor(@RequestBody SysTask sysTask) {
        Long name = sysTask.getProject();
        Long auditor = sysTask.getAuditor();
        String auditorName = sysTask.getAuditorName();
        LambdaUpdateWrapper<SysTask> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(SysTask::getName, name)
                .set(SysTask::getProjectName, auditorName)
                .eq(SysTask::getProject, auditor);
        boolean ret = taskService.update(updateWrapper);
        return R.success();
    }

    /**
     * 提交审核  编目中、待修改->审核中
     * @param taskId 任务id
     * @return {@link R}
     */
    @PreAuthorize("hasRole('CATALOGER')")
    @GetMapping("/submit")
    public R submitAudit(Integer taskId) {
        if (ObjectUtil.isNotNull(taskId)) {
            boolean ret = taskService.lambdaUpdate()
                    .set(SysTask::getStatus, TaskState.PADDING.getState())
                    .eq(SysTask::getId, taskId)
                    .and(i -> i.eq(SysTask::getStatus, TaskState.CATALOG.getState())
                            .or().eq(SysTask::getStatus, TaskState.MODEIFY.getState()))
                    .update();
            if (ret) {
                return R.success("提交审核成功");
            }
        }
        return R.error("参数错误");
    }


    /**
     * 打回编目  审核中->待修改
     * @param taskId 任务id
     * @return {@link R}
     */
    @PreAuthorize("hasRole('AUDITOR')")
    @GetMapping("/reback")
    public R rebackCatalog(Integer taskId) {
        boolean ret = taskService.lambdaUpdate()
                .set(SysTask::getStatus, TaskState.MODEIFY.getState())
                .eq(SysTask::getStatus, TaskState.PADDING.getState())
                .eq(SysTask::getId, taskId).update();
        return R.success("审核完成");
    }


    /**
     * 通过编目  审核中->完成
     * @param taskId 任务id
     * @return {@link R}
     */
    @PreAuthorize("hasRole('AUDITOR')")
    @GetMapping("/pass")
    public R passCatalog(Integer taskId) {
        boolean ret = taskService.lambdaUpdate()
                .set(SysTask::getStatus, TaskState.PADDING.getState())
                .eq(SysTask::getStatus, TaskState.FINISHED.getState())
                .eq(SysTask::getId, taskId).update();
        return R.success("审核通过");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/query/name")
    public R queryByName(String name) {
        LambdaQueryWrapper<SysTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysTask::getName, name);
        SysTask target = taskService.getOne(queryWrapper);
        return R.success(target);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/query/project")
    public R queryByProject(@RequestParam(required = true) Integer projectId,
                            @RequestParam(required = false, defaultValue = "1") Integer current,
                            @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        LambdaQueryWrapper<SysTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysTask::getProject, projectId)
                .orderByDesc(SysTask::getCreateTime);
        Page<SysTask> target = taskService.page(new Page<SysTask>(current, pageSize), queryWrapper);
        return R.success(target);
    }

    @PreAuthorize("hasAnyRole('CATALOGER','AUDITOR')")
    @GetMapping("/query/user")
    public R queryByAccount(String account, Integer projectId,
                            @RequestParam(required = false, defaultValue = "1") Integer current,
                            @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        Page<SysTask> page = taskService.queryByAccount(account, projectId, current, pageSize);
        return R.success(page);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/query/cataloger")
    public R queryByCataloger(Integer catalogerId) {
        LambdaQueryWrapper<SysTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysTask::getCataloger, catalogerId);
        SysTask target = taskService.getOne(queryWrapper);
        return R.success(target);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/query/auditor")
    public R queryByAuditor(Integer auditorId) {
        LambdaQueryWrapper<SysTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysTask::getAuditor, auditorId);
        SysTask target = taskService.getOne(queryWrapper);
        return R.success(target);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/query/status")
    public R queryTaskList(@RequestParam(required = false, defaultValue = "编目中") String status,
                           @RequestParam(required = false, defaultValue = "account") String order,
                           @RequestParam(required = false, defaultValue = "1") Integer isAsc,
                           @RequestParam(required = false, defaultValue = "0") Integer current,
                           @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        QueryWrapper<SysTask> queryWrapper = new QueryWrapper<SysTask>();
        queryWrapper.eq("status", TaskState.valueOf(status).getState())
                .orderBy(true, isAsc > 0 ? true : false, order);
        Page<SysTask> page = taskService.page(new Page<SysTask>(current, pageSize),
                queryWrapper);
        return R.success(page);
    }
}

