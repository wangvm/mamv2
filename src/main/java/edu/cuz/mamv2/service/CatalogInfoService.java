package edu.cuz.mamv2.service;

import edu.cuz.mamv2.entity.dto.FragmentDTO;
import edu.cuz.mamv2.entity.dto.ProgramDTO;
import edu.cuz.mamv2.entity.dto.ScenesDTO;
import edu.cuz.mamv2.utils.BackMessage;

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
    BackMessage addProgramRecord(ProgramDTO program);

    BackMessage addFragmentRecord(FragmentDTO fragment);

    BackMessage addScenesRecord(ScenesDTO scenese);

    BackMessage deleteCatalogRecord(String catalogId, String record);

    BackMessage getCatalogRecord(String record, String catalogId);

    BackMessage getMenu(Integer taskId);

    BackMessage updateProgramRecord(ProgramDTO program);

    BackMessage updateFragmentRecord(FragmentDTO fragment);

    BackMessage updateScenesRecord(ScenesDTO scenese);
}
