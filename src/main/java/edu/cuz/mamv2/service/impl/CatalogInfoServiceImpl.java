package edu.cuz.mamv2.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import edu.cuz.mamv2.entity.dto.*;
import edu.cuz.mamv2.exception.ServiceException;
import edu.cuz.mamv2.repository.FragmentRepository;
import edu.cuz.mamv2.repository.ProgramRepository;
import edu.cuz.mamv2.repository.ScenesRepository;
import edu.cuz.mamv2.service.CatalogInfoService;
import edu.cuz.mamv2.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * 服务实现类
 * </p>
 * @author VM
 * @since 2022/03/04 05:00
 */
@Service
@Slf4j
public class CatalogInfoServiceImpl implements CatalogInfoService {
    @Resource
    private ProgramRepository programRepository;
    @Resource
    private FragmentRepository fragmentRepository;
    @Resource
    private ScenesRepository scenesRepository;
    @Resource
    private RestHighLevelClient restHighLevelClient;


    @Override
    public ProgramDTO addProgramRecord(ProgramDTO program) {
        Optional<ProgramDTO> programDTO = programRepository.findByTaskId(program.getTaskId());
        if (programDTO.isPresent()) {
            throw new ServiceException("任务已存在");
        }
        ProgramDTO save = programRepository.save(program);
        return save;
    }

    @Override
    public FragmentDTO addFragmentRecord(FragmentDTO fragment) {
        FragmentDTO save = fragmentRepository.save(fragment);
        return save;
    }

    @Override
    public ScenesDTO addScenesRecord(ScenesDTO scenese) {
        ScenesDTO save = scenesRepository.save(scenese);
        return save;
    }

    @Override
    public void deleteCatalogRecord(String catalogId, String record) {
        switch (record) {
            case "fragment":
                fragmentRepository.deleteById(catalogId);
                break;
            case "scenes":
                scenesRepository.deleteById(catalogId);
                break;
            default:
                throw new ServiceException("删除类型不存在");
        }
    }

    @Override
    public R getCatalogRecord(String record, String catalogId) {
        switch (record) {
            case "fragment":
                Optional<FragmentDTO> fragment = fragmentRepository.findById(catalogId);
                if (fragment.isPresent()) {
                    return R.success(fragment);
                } else {
                    return R.error("数据不存在");
                }
            case "scenes":
                Optional<ScenesDTO> scenes = scenesRepository.findById(catalogId);
                if (scenes.isPresent()) {
                    return R.success(scenes.get());
                } else {
                    return R.error("数据不存在");
                }
            default:
                return R.error("类型不存在");
        }
    }

    @Override
    public List<MenuVO> getMenu(Integer taskId) {
        // 构建搜索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.fetchSource(new String[]{"id", "menu"}, null)
                .query(QueryBuilders.termQuery("taskId", taskId));
        // 构造搜索请求
        SearchRequest searchRequest = new SearchRequest("program", "fragment", "scenes");
        searchRequest.source(sourceBuilder);
        SearchResponse response;
        try {
            // 获取搜索结果
            response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("查询失败，请重试");
        }
        log.info("检索任务id：{}，检索文档总数：{}，最大得分：{}", taskId,
                response.getHits().getTotalHits(),
                response.getHits().getMaxScore());
        SearchHit[] hits = response.getHits().getHits();
        ArrayList<MenuVO> menus = new ArrayList<>();
        for (SearchHit hit: hits) {
            String id = hit.getId();
            Map<String, Object> map = hit.getSourceAsMap();
            Menu menu = JSONObject.parseObject(JSON.toJSONString(map.get("menu")), Menu.class);
            MenuVO menuVO = new MenuVO(id, menu);
            menus.add(menuVO);
        }
        return menus;
    }

    @Override
    public ProgramDTO updateProgramRecord(ProgramDTO program) {
        ProgramDTO save = programRepository.save(program);
        return save;
    }

    @Override
    public FragmentDTO updateFragmentRecord(FragmentDTO fragment) {
        FragmentDTO save = fragmentRepository.save(fragment);
        return save;
    }

    @Override
    public ScenesDTO updateScenesRecord(ScenesDTO scenese) {
        ScenesDTO save = scenesRepository.save(scenese);
        return save;
    }

    @Override
    public int deleteBulkScenes(List<String> scenesList) {
        DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest("scenes");
        deleteByQueryRequest.setConflicts("proceed");
        deleteByQueryRequest.setQuery(QueryBuilders.termsQuery("_id", scenesList));
        BulkByScrollResponse response = null;
        try {
            response = restHighLevelClient.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.info("批量删除场景层数据失败,message:{}", e.getMessage());
            throw new ServiceException("删除失败");
        }
        return response.getBatches();
    }

    @Override
    public ProgramDTO getProgramRecord(String catalogId, Long taskId) {
        Optional<ProgramDTO> program;
        if (catalogId == null) {
            program = programRepository.findByTaskId(taskId);
        } else {
            program = programRepository.findById(catalogId);
        }
        return program.get();
    }
}
