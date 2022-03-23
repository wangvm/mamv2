package edu.cuz.mamv2.repository;

import edu.cuz.mamv2.entity.dto.VideoDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author VM
 * @date 2022/3/5 21:08
 */
public interface VideoRepository extends ElasticsearchRepository<VideoDTO, String> {
}
