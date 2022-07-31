package edu.cuz.mamv2.service;

import edu.cuz.mamv2.entity.MamFragment;
import edu.cuz.mamv2.entity.MamProgram;
import edu.cuz.mamv2.entity.MamScenes;
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
    MamProgram addProgramRecord(MamProgram program);

    MamFragment addFragmentRecord(MamFragment fragment);

    MamScenes addScenesRecord(MamScenes scenese);

    void deleteCatalogRecord(String catalogId, String record);

    R getCatalogRecord(String record, String catalogId);

    List<MenuVO> getMenu(Integer taskId);

    MamProgram updateProgramRecord(MamProgram program);

    MamFragment updateFragmentRecord(MamFragment fragment);

    MamScenes updateScenesRecord(MamScenes scenese);

    int deleteBulkScenes(List<String> scenesList);

    MamProgram getProgramRecord(String catalogId, Long taskId);
}
