package edu.cuz.mamv2.repository;

import edu.cuz.mamv2.entity.dto.ProgramDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

/**
 * @author VM
 * @date 2022/2/23 23:23
 * @description
 */
public interface ProgramRepository extends ElasticsearchRepository<ProgramDTO, String> {
    /**
     * 查询任务是否存在
     * @param taskId 查询的任务id
     * @return 任务是否存在
     */
    Optional<ProgramDTO> findByTaskId(Long taskId);

    void deleteByTaskId(String catalogId);
}
