package edu.cuz.mamv2.controller;


import edu.cuz.mamv2.entity.MamFragment;
import edu.cuz.mamv2.entity.MamProgram;
import edu.cuz.mamv2.entity.MamScenes;
import edu.cuz.mamv2.entity.dto.*;
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

    /**
     * 添加编目记录
     */
    @PreAuthorize("hasRole('CATALOGER')")
    @PostMapping("/add/program")
    public R addProgramRecord(@RequestBody MamProgram program) {
        return R.toResult(catalogInfoService.addProgramRecord(program));
    }

    @PreAuthorize("hasRole('CATALOGER')")
    @PostMapping("/add/fragment")
    public R addFragmentRecord(@RequestBody MamFragment fragment) {
        return R.toResult(catalogInfoService.addFragmentRecord(fragment));
    }

    @PreAuthorize("hasRole('CATALOGER')")
    @PostMapping("/add/scenes")
    public R addScenesRecord(@RequestBody MamScenes scenese) {
        return R.toResult(catalogInfoService.addScenesRecord(scenese));
    }

    // 删除编目记录
    @PreAuthorize("hasRole('CATALOGER')")
    @GetMapping("/delete/{record}")
    public R deleteCatalogRecord(@PathVariable("record") String record,
                                 @RequestParam String catalogId) {
        catalogInfoService.deleteCatalogRecord(catalogId, record);
        return R.success();
    }

    @PreAuthorize("hasRole('CATALOGER')")
    @PostMapping("/delete/bulk/scenes")
    public R deleteBulkScenes(@RequestBody List<String> scenesList) {
        return R.toResult(catalogInfoService.deleteBulkScenes(scenesList));
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
        return R.success(catalogInfoService.getProgramRecord(catalogId, taskId));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get/menu")
    public R getMenu(Integer taskId) {
        List<MenuVO> menus = catalogInfoService.getMenu(taskId);
        // todo 构建为树结构再返回
        return R.success(menus);
    }

    // 更新具体的记录
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update/program")
    public R updateProgramRecord(@RequestBody MamProgram program) {
        return R.toResult(catalogInfoService.updateProgramRecord(program));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update/fragment")
    public R updateFragmentRecord(@RequestBody MamFragment fragment) {
        return R.toResult(catalogInfoService.updateFragmentRecord(fragment));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update/scenes")
    public R updateScenesRecord(@RequestBody MamScenes scenese) {
        return R.toResult(catalogInfoService.updateScenesRecord(scenese));
    }
}

