package edu.cuz.mamv2.service;

import edu.cuz.mamv2.entity.dto.FragmentDTO;
import edu.cuz.mamv2.entity.dto.ProgramDTO;
import edu.cuz.mamv2.entity.dto.ScenesDTO;
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
    R addProgramRecord(ProgramDTO program);

    R addFragmentRecord(FragmentDTO fragment);

    R addScenesRecord(ScenesDTO scenese);

    R deleteCatalogRecord(String catalogId, String record);

    R getCatalogRecord(String record, String catalogId);

    R getMenu(Integer taskId);

    R updateProgramRecord(ProgramDTO program);

    R updateFragmentRecord(FragmentDTO fragment);

    R updateScenesRecord(ScenesDTO scenese);

    R deleteBulkScenes(List<String> scenesList);

    R getProgramRecord(String catalogId, Long taskId);
}
