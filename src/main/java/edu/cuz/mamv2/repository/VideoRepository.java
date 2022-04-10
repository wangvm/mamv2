package edu.cuz.mamv2.repository;

import edu.cuz.mamv2.entity.dto.VideoDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author VM
 * @date 2022/3/5 21:08
 */
public interface VideoRepository extends ElasticsearchRepository<VideoDTO, String> {
    /**
     * 找到文件名字
     * @param name 名字
     * @return {@link VideoDTO}
     */
    VideoDTO findByFileName(String name);
}
