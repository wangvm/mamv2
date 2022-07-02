package edu.cuz.mamv2.controller;


import edu.cuz.mamv2.entity.dto.FragmentDTO;
import edu.cuz.mamv2.entity.dto.ProgramDTO;
import edu.cuz.mamv2.entity.dto.ScenesDTO;
import edu.cuz.mamv2.service.CatalogInfoService;
import edu.cuz.mamv2.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 * @author VM
 * @since 2022/03/04 05:00
 */
@RestController
@Slf4j
@RequestMapping("/catalog")
public class CatalogInfoController {
    @Resource
    private CatalogInfoService catalogInfoService;

    // 添加编目记录
    @PreAuthorize("hasRole('CATALOGER')")
    @PostMapping("/add/program")
    public R addProgramRecord(@RequestBody ProgramDTO program) {
        R backMessage = catalogInfoService.addProgramRecord(program);
        return backMessage;
    }

    @PreAuthorize("hasRole('CATALOGER')")
    @PostMapping("/add/fragment")
    public R addFragmentRecord(@RequestBody FragmentDTO fragment) {
        R backMessage = catalogInfoService.addFragmentRecord(fragment);
        return backMessage;
    }

    @PreAuthorize("hasRole('CATALOGER')")
    @PostMapping("/add/scenes")
    public R addScenesRecord(@RequestBody ScenesDTO scenese) {
        R backMessage = catalogInfoService.addScenesRecord(scenese);
        return backMessage;
    }

    // 删除编目记录
    @PreAuthorize("hasRole('CATALOGER')")
    @GetMapping("/delete/{record}")
    public R deleteCatalogRecord(@PathVariable("record") String record,
                                 @RequestParam String catalogId) {
        R backMessage = catalogInfoService.deleteCatalogRecord(catalogId, record);
        return backMessage;
    }

    @PreAuthorize("hasRole('CATALOGER')")
    @PostMapping("/delete/bulk/scenes")
    public R deleteBulkScenes(@RequestBody List<String> scenesList) {
        R backMessage = catalogInfoService.deleteBulkScenes(scenesList);
        return backMessage;
    }

    // 获取具体记录
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get/{record}")
    public R getCatalogRecord(@PathVariable("record") String record,
                              @RequestParam String catalogId) {
        R backMessage = catalogInfoService.getCatalogRecord(record, catalogId);
        return backMessage;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get/program")
    public R getProgramRecord(@RequestParam(required = false) String catalogId,
                              @RequestParam(required = false) Long taskId) {
        R backMessage = catalogInfoService.getProgramRecord(catalogId, taskId);
        return backMessage;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get/menu")
    public R getMenu(Integer taskId) {
        R backMessage = catalogInfoService.getMenu(taskId);
        return backMessage;
    }

    // 更新具体的记录
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update/program")
    public R updateProgramRecord(@RequestBody ProgramDTO program) {
        R backMessage = catalogInfoService.updateProgramRecord(program);
        return backMessage;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update/fragment")
    public R updateFragmentRecord(@RequestBody FragmentDTO fragment) {
        R backMessage = catalogInfoService.updateFragmentRecord(fragment);
        return backMessage;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update/scenes")
    public R updateScenesRecord(@RequestBody ScenesDTO scenese) {
        R backMessage = catalogInfoService.updateScenesRecord(scenese);
        return backMessage;
    }
}

