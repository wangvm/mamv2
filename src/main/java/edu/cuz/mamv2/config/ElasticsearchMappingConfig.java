package edu.cuz.mamv2.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

import javax.annotation.Resource;

/**
 * @author VM
 * @date 2022/3/11 15:47
 */
public class ElasticsearchMappingConfig implements ApplicationRunner {
    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createVideoInfoMapping();
    }

    private void createVideoInfoMapping() {
        // boolean index = elasticsearchRestTemplate.createIndex(VideoDTO.class);
    }
}
