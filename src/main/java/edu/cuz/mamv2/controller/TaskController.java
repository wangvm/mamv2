package edu.cuz.mamv2.controller;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.cuz.mamv2.entity.Task;
import edu.cuz.mamv2.entity.TaskDTO;
import edu.cuz.mamv2.entity.dto.*;
import edu.cuz.mamv2.enums.TaskState;
import edu.cuz.mamv2.repository.ProgramRepository;
import edu.cuz.mamv2.service.TaskService;
import edu.cuz.mamv2.utils.BackEnum;
import edu.cuz.mamv2.utils.BackMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 媒资任务表 前端控制器
 * </p>
 * @author VM
 * @since 2022/01/17 10:56
 */
@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskService taskService;
    @Resource
    private ProgramRepository programRepository;

    @PostMapping("/add")
    public BackMessage addTask(@RequestBody TaskDTO taskDTO) {
        Task task = taskDTO.getTaskInfo();
        task.setCreateTime(System.currentTimeMillis());
        boolean ret = taskService.save(task);
        // todo 添加初始化编目节目层数据操作
        VideoDTO videoInfo = taskDTO.getVideoInfo();
        String filename = videoInfo.getFileName()
                .replaceAll("\\\\\"<span style='color:red'>\\\\\"|\\\\\"</span>\\\\\"", "");
        ProgramDTO program = new ProgramDTO();
        program.setTaskId(task.getId());
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setId(1L);
        menuDTO.setCheck(0);
        menuDTO.setContent(filename);
        menuDTO.setLevel("节目层");
        menuDTO.setParent(1L);
        program.setMenu(menuDTO);
        program.setTitle(new Attributes(filename));
        program.setAspectRatio(new Attributes(videoInfo.getAspectRatio().asEncoderArgument()));
        program.setAudioChannel(new Attributes(videoInfo.getAudioChannel().toString()));
        ProgramDTO b = programRepository.findByTaskId(program.getTaskId());
        if (b != null) {
            return new BackMessage().failureWithMessage("任务已存在");
        }
        ProgramDTO save = programRepository.save(program);
        if (save == null) {
            return new BackMessage().failureWithMessage("添加失败，请重试");
        }
        if (ret) {
            return new BackMessage(BackEnum.SUCCESS);
        } else {
            return new BackMessage(BackEnum.BAD_REQUEST);
        }
    }

    @PostMapping("/addMany")
    public BackMessage addManyTask(@RequestBody ValidationList<Task> tasks) {
        for (Task task: tasks) {
            task.setCreateTime(System.currentTimeMillis());
        }
        boolean ret = taskService.saveBatch(tasks);
        if (ret) {
            return new BackMessage(BackEnum.SUCCESS);
        } else {
            return new BackMessage(BackEnum.BAD_REQUEST);
        }
    }

    @PostMapping("/delete")
    public BackMessage deleteTask(@RequestBody Task task) {
        Long id = task.getId();
        boolean ret = taskService.removeById(id);
        if (ret) {
            return new BackMessage(BackEnum.SUCCESS);
        } else {
            return new BackMessage(BackEnum.BAD_REQUEST);
        }
    }

    //    更改任务名 /task/update/name
    @PostMapping("/update/name")
    public BackMessage updateName(@RequestBody Task task) {
        String name = task.getName();
        Long id = task.getId();
        LambdaUpdateWrapper<Task> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Task::getName, name)
                .eq(Task::getId, id);
        boolean ret = taskService.update(updateWrapper);
        if (ret) {
            return new BackMessage(BackEnum.SUCCESS);
        } else {
            return new BackMessage(BackEnum.BAD_REQUEST);
        }
    }

    //    更改所属项目 /task/update/project
    @Deprecated
    @PostMapping("/update/project")
    public BackMessage updateProject(@RequestBody Task task) {
        Long name = task.getProject();
        Long projectId = task.getProject();
        String projectName = task.getProjectName();
        LambdaUpdateWrapper<Task> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Task::getName, name)
                .set(Task::getProjectName, projectName)
                .eq(Task::getProject, projectId);
        boolean ret = taskService.update(updateWrapper);
        if (ret) {
            return new BackMessage(BackEnum.SUCCESS);
        } else {
            return new BackMessage(BackEnum.BAD_REQUEST);
        }
    }


    @PostMapping("/update")
    public BackMessage updateTaskInfo(@RequestBody Task task) {
        boolean ret = taskService.updateById(task);
        if (ret) {
            return new BackMessage(BackEnum.SUCCESS);
        } else {
            return new BackMessage(BackEnum.DATA_ERROR);
        }
    }

    //    更改编目员
    @PostMapping("/update/cataloger")
    public BackMessage updateCataloger(@RequestBody Task task) {
        Long name = task.getProject();
        Long cataloger = task.getCataloger();
        String catalogerName = task.getCatalogerName();
        LambdaUpdateWrapper<Task> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Task::getName, name)
                .set(Task::getProjectName, catalogerName)
                .eq(Task::getProject, cataloger);
        boolean ret = taskService.update(updateWrapper);
        if (ret) {
            return new BackMessage(BackEnum.SUCCESS);
        } else {
            return new BackMessage(BackEnum.BAD_REQUEST);
        }
    }

    //    更改审核员
    @PostMapping("/update/auditor")
    public BackMessage updateAuditor(@RequestBody Task task) {
        Long name = task.getProject();
        Long auditor = task.getAuditor();
        String auditorName = task.getAuditorName();
        LambdaUpdateWrapper<Task> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Task::getName, name)
                .set(Task::getProjectName, auditorName)
                .eq(Task::getProject, auditor);
        boolean ret = taskService.update(updateWrapper);
        if (ret) {
            return new BackMessage(BackEnum.SUCCESS);
        } else {
            return new BackMessage(BackEnum.BAD_REQUEST);
        }
    }

    //    提交审核  0->1
    @GetMapping("/submit")
    public BackMessage submitAudit(Integer taskId) {
        if (ObjectUtil.isNotNull(taskId)) {
            boolean ret = taskService.lambdaUpdate()
                    .set(Task::getStatus, TaskState.PADDING.getCode())
                    .eq(Task::getStatus, TaskState.CATALOG.getCode())
                    .eq(Task::getId, taskId).update();
            if (ret) {
                return new BackMessage(BackEnum.SUCCESS);
            }
        }
        return new BackMessage(BackEnum.BAD_REQUEST);
    }

    //    打回编目  1->0
    @GetMapping("/reback")
    public BackMessage rebackCatalog(Integer taskId) {
        if (ObjectUtil.isNotNull(taskId)) {
            boolean ret = taskService.lambdaUpdate()
                    .set(Task::getStatus, TaskState.CATALOG.getCode())
                    .eq(Task::getStatus, TaskState.PADDING.getCode())
                    .eq(Task::getId, taskId).update();
            if (ret) {
                return new BackMessage(BackEnum.SUCCESS);
            }
        }
        return new BackMessage(BackEnum.BAD_REQUEST);
    }

    //    通过编目  1->2
    @GetMapping("/pass")
    public BackMessage passCatalog(Integer taskId) {
        if (ObjectUtil.isNotNull(taskId)) {
            boolean ret = taskService.lambdaUpdate()
                    .set(Task::getStatus, TaskState.PADDING.getCode())
                    .eq(Task::getStatus, TaskState.FINISHED.getCode())
                    .eq(Task::getId, taskId).update();
            if (ret) {
                return new BackMessage(BackEnum.SUCCESS);
            }
        }
        return new BackMessage(BackEnum.BAD_REQUEST);
    }

    //    任务名查询
    @GetMapping("/query/name")
    public BackMessage queryByName(String name) {
        if (ObjectUtil.isNotEmpty(name)) {
            LambdaQueryWrapper<Task> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Task::getName, name);
            Task target = taskService.getOne(queryWrapper);
            if (target != null) {
                return new BackMessage(BackEnum.SUCCESS, target);
            }
        }
        return new BackMessage(BackEnum.BAD_REQUEST);
    }

    //    归属项目查询
    @GetMapping("/query/project")
    public BackMessage queryByProject(@RequestParam(required = true) Integer projectId,
                                      @RequestParam(required = false, defaultValue = "1") Integer current,
                                      @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        if (ObjectUtil.isNotEmpty(projectId)) {
            LambdaQueryWrapper<Task> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Task::getProject, projectId)
                    .orderByDesc(Task::getCreateTime);
            Page<Task> target = taskService.page(new Page<Task>(current, pageSize), queryWrapper);
            if (target != null) {
                return new BackMessage(BackEnum.SUCCESS, target);
            } else {
                return new BackMessage(200, "项目暂无任务");
            }
        }
        return new BackMessage(BackEnum.BAD_REQUEST);
    }

    //    编目员查询
    @GetMapping("/query/cataloger")
    public BackMessage queryByCataloger(Integer catalogerId) {
        if (ObjectUtil.isNotEmpty(catalogerId)) {
            LambdaQueryWrapper<Task> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Task::getCataloger, catalogerId);
            Task target = taskService.getOne(queryWrapper);
            if (target != null) {
                return new BackMessage(BackEnum.SUCCESS, target);
            }
        }
        return new BackMessage(BackEnum.BAD_REQUEST);
    }

    //    审核员查询
    @GetMapping("/query/auditor")
    public BackMessage queryByAuditor(Integer auditorId) {
        if (ObjectUtil.isNotEmpty(auditorId)) {
            LambdaQueryWrapper<Task> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Task::getAuditor, auditorId);
            Task target = taskService.getOne(queryWrapper);
            if (target != null) {
                return new BackMessage(BackEnum.SUCCESS, target);
            }
        }
        return new BackMessage(BackEnum.BAD_REQUEST);
    }

    //    状态查询 0：编目中、1：审核中、2：完成
    @GetMapping("/query/status")
    public BackMessage queryTaskList(@RequestParam(required = false, defaultValue = "CATALOG") String status,
                                     @RequestParam(required = false, defaultValue = "account") String order,
                                     @RequestParam(required = false, defaultValue = "1") Integer isAsc,
                                     @RequestParam(required = false, defaultValue = "0") Integer current,
                                     @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<Task>();
        queryWrapper.eq("status", TaskState.valueOf(status).getCode())
                .orderBy(true, isAsc > 0 ? true : false, order);
        Page<Task> page = taskService.page(new Page<Task>(current, pageSize),
                queryWrapper);
        return new BackMessage(BackEnum.SUCCESS, page);
    }
}

