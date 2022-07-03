package edu.cuz.mamv2.controller;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.cuz.mamv2.entity.MamTask;
import edu.cuz.mamv2.entity.TaskDTO;
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
        MamTask mamTask = taskDTO.getTaskInfo();
        VideoDTO videoInfo = taskDTO.getVideoInfo();
        mamTask.setCreateTime(System.currentTimeMillis());
        mamTask.setVideoInfoId(videoInfo.getId());
        boolean ret = taskService.save(mamTask);
        String filename = videoInfo.getFileName();
        ProgramDTO program = new ProgramDTO();
        program.setTaskId(mamTask.getId());
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
        Optional<ProgramDTO> b = programRepository.findByTaskId(program.getTaskId());
        if (b.isPresent()) {
            return R.error("任务已存在");
        }
        ProgramDTO save = programRepository.save(program);
        if (save == null) {
            return R.error("添加失败，请重试");
        }
        return R.success("添加任务成功");

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addMany")
    public R addManyTask(@RequestBody ValidationList<MamTask> mamTasks) {
        for (MamTask mamTask: mamTasks) {
            mamTask.setCreateTime(System.currentTimeMillis());
        }
        boolean ret = taskService.saveBatch(mamTasks);
        return R.success();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete")
    public R deleteTask(@RequestBody MamTask mamTask) {
        Long id = mamTask.getId();
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
    public R updateName(@RequestBody MamTask mamTask) {
        String name = mamTask.getName();
        Long id = mamTask.getId();
        LambdaUpdateWrapper<MamTask> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(MamTask::getName, name)
                .eq(MamTask::getId, id);
        boolean ret = taskService.update(updateWrapper);
        return R.success();
    }

    @Deprecated
    @PostMapping("/update/project")
    public R updateProject(@RequestBody MamTask mamTask) {
        Long name = mamTask.getProject();
        Long projectId = mamTask.getProject();
        String projectName = mamTask.getProjectName();
        LambdaUpdateWrapper<MamTask> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(MamTask::getName, name)
                .set(MamTask::getProjectName, projectName)
                .eq(MamTask::getProject, projectId);
        boolean ret = taskService.update(updateWrapper);
        return R.success();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update")
    public R updateTaskInfo(@RequestBody MamTask mamTask) {
        boolean ret = taskService.updateById(mamTask);
        return R.success("更新成功");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update/cataloger")
    public R updateCataloger(@RequestBody MamTask mamTask) {
        Long name = mamTask.getProject();
        Long cataloger = mamTask.getCataloger();
        String catalogerName = mamTask.getCatalogerName();
        LambdaUpdateWrapper<MamTask> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(MamTask::getName, name)
                .set(MamTask::getProjectName, catalogerName)
                .eq(MamTask::getProject, cataloger);
        boolean ret = taskService.update(updateWrapper);
        return R.success();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update/auditor")
    public R updateAuditor(@RequestBody MamTask mamTask) {
        Long name = mamTask.getProject();
        Long auditor = mamTask.getAuditor();
        String auditorName = mamTask.getAuditorName();
        LambdaUpdateWrapper<MamTask> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(MamTask::getName, name)
                .set(MamTask::getProjectName, auditorName)
                .eq(MamTask::getProject, auditor);
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
                    .set(MamTask::getStatus, TaskState.PADDING.getState())
                    .eq(MamTask::getId, taskId)
                    .and(i -> i.eq(MamTask::getStatus, TaskState.CATALOG.getState())
                            .or().eq(MamTask::getStatus, TaskState.MODEIFY.getState()))
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
                .set(MamTask::getStatus, TaskState.MODEIFY.getState())
                .eq(MamTask::getStatus, TaskState.PADDING.getState())
                .eq(MamTask::getId, taskId).update();
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
                .set(MamTask::getStatus, TaskState.PADDING.getState())
                .eq(MamTask::getStatus, TaskState.FINISHED.getState())
                .eq(MamTask::getId, taskId).update();
        return R.success("审核通过");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/query/name")
    public R queryByName(String name) {
        LambdaQueryWrapper<MamTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MamTask::getName, name);
        MamTask target = taskService.getOne(queryWrapper);
        return R.success(target);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/query/project")
    public R queryByProject(@RequestParam(required = true) Integer projectId,
                            @RequestParam(required = false, defaultValue = "1") Integer current,
                            @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        LambdaQueryWrapper<MamTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MamTask::getProject, projectId)
                .orderByDesc(MamTask::getCreateTime);
        Page<MamTask> target = taskService.page(new Page<MamTask>(current, pageSize), queryWrapper);
        return R.success(target);
    }

    @PreAuthorize("hasAnyRole('CATALOGER','AUDITOR')")
    @GetMapping("/query/user")
    public R queryByAccount(String account, Integer projectId,
                            @RequestParam(required = false, defaultValue = "1") Integer current,
                            @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        Page<MamTask> page = taskService.queryByAccount(account, projectId, current, pageSize);
        return R.success(page);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/query/cataloger")
    public R queryByCataloger(Integer catalogerId) {
        LambdaQueryWrapper<MamTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MamTask::getCataloger, catalogerId);
        MamTask target = taskService.getOne(queryWrapper);
        return R.success(target);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/query/auditor")
    public R queryByAuditor(Integer auditorId) {
        LambdaQueryWrapper<MamTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MamTask::getAuditor, auditorId);
        MamTask target = taskService.getOne(queryWrapper);
        return R.success(target);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/query/status")
    public R queryTaskList(@RequestParam(required = false, defaultValue = "编目中") String status,
                           @RequestParam(required = false, defaultValue = "account") String order,
                           @RequestParam(required = false, defaultValue = "1") Integer isAsc,
                           @RequestParam(required = false, defaultValue = "0") Integer current,
                           @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        QueryWrapper<MamTask> queryWrapper = new QueryWrapper<MamTask>();
        queryWrapper.eq("status", TaskState.valueOf(status).getState())
                .orderBy(true, isAsc > 0 ? true : false, order);
        Page<MamTask> page = taskService.page(new Page<MamTask>(current, pageSize),
                queryWrapper);
        return R.success(page);
    }
}

