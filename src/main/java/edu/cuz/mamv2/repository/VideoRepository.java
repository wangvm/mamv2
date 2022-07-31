package edu.cuz.mamv2.repository;

import edu.cuz.mamv2.entity.VideoInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author VM
 * @date 2022/3/5 21:08
 */
public interface VideoRepository extends ElasticsearchRepository<VideoInfo, String> {
    /**
     * 找到文件名字
     * @param name 名字
     * @return {@link VideoInfo}
     */
    VideoInfo findByFileName(String name);
}
