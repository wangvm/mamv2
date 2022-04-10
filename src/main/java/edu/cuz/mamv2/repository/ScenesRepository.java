package edu.cuz.mamv2.repository;

import edu.cuz.mamv2.entity.dto.ScenesDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author VM
 * @date 2022/3/4 21:56
 */
public interface ScenesRepository extends ElasticsearchRepository<ScenesDTO, String> {
}
