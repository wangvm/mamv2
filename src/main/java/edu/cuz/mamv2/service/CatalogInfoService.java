package edu.cuz.mamv2.service;

import edu.cuz.mamv2.entity.dto.*;
import edu.cuz.mamv2.utils.R;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 * @author VM
 * @since 2022/03/04 05:00
 */
public interface CatalogInfoService {
    /**
     * 添加节目层数据
     * @param program 节目层实体
     * @return 添加结果
     */
    ProgramDTO addProgramRecord(ProgramDTO program);

    FragmentDTO addFragmentRecord(FragmentDTO fragment);

    ScenesDTO addScenesRecord(ScenesDTO scenese);

    void deleteCatalogRecord(String catalogId, String record);

    R getCatalogRecord(String record, String catalogId);

    List<MenuVO> getMenu(Integer taskId);

    ProgramDTO updateProgramRecord(ProgramDTO program);

    FragmentDTO updateFragmentRecord(FragmentDTO fragment);

    ScenesDTO updateScenesRecord(ScenesDTO scenese);

    int deleteBulkScenes(List<String> scenesList);

    ProgramDTO getProgramRecord(String catalogId, Long taskId);
}
