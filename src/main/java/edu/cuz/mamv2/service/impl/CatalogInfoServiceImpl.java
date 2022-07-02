package edu.cuz.mamv2.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import edu.cuz.mamv2.entity.dto.*;
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
    public R addProgramRecord(ProgramDTO program) {
        Optional<ProgramDTO> b = programRepository.findByTaskId(program.getTaskId());
        if (b.isPresent()) {
            return R.error("任务已存在");
        }
        ProgramDTO save = programRepository.save(program);
        if (save == null) {
            return R.error("添加失败，请重试");
        }
        return R.success();
    }

    @Override
    public R addFragmentRecord(FragmentDTO fragment) {
        FragmentDTO save = fragmentRepository.save(fragment);
        if (save == null) {
            return R.error("添加失败，请重试");
        }
        return R.success(save);
    }

    @Override
    public R addScenesRecord(ScenesDTO scenese) {
        ScenesDTO save = scenesRepository.save(scenese);
        if (save == null) {
            return R.error("添加失败，请重试");
        }
        return R.success(save);
    }

    @Override
    public R deleteCatalogRecord(String catalogId, String record) {
        switch (record) {
            case "fragment":
                fragmentRepository.deleteById(catalogId);
                break;
            case "scenes":
                scenesRepository.deleteById(catalogId);
                break;
            default:
                return R.error("删除类型不存在");
        }
        return R.success();
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
                    return R.success(scenes);
                } else {
                    return R.error("数据不存在");
                }
            default:
                return R.error("类型不存在");
        }
    }

    @Override
    public R getMenu(Integer taskId) {
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
            return R.error("查询失败请重试");
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
        return R.success(menus);
    }

    @Override
    public R updateProgramRecord(ProgramDTO program) {
        ProgramDTO save = programRepository.save(program);
        if (save == null) {
            return R.error("更新失败，请重试");
        }
        return R.success("更新成功");
    }

    @Override
    public R updateFragmentRecord(FragmentDTO fragment) {
        FragmentDTO save = fragmentRepository.save(fragment);
        if (save == null) {
            return R.error("更新失败，请重试");
        }
        return R.success("更新成功");
    }

    @Override
    public R updateScenesRecord(ScenesDTO scenese) {
        ScenesDTO save = scenesRepository.save(scenese);
        if (save == null) {
            return R.error("更新失败，请重试");
        }
        return R.success();
    }

    @Override
    public R deleteBulkScenes(List<String> scenesList) {
        DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest("scenes");
        deleteByQueryRequest.setConflicts("proceed");
        deleteByQueryRequest.setQuery(QueryBuilders.termsQuery("_id", scenesList));
        BulkByScrollResponse response = null;
        try {
            response = restHighLevelClient.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.info("批量删除场景层数据失败,message:{}", e.getMessage());
            return R.error(e.getMessage());
        }
        return R.success(response);
    }

    @Override
    public R getProgramRecord(String catalogId, Long taskId) {
        Optional<ProgramDTO> program;
        if (catalogId == null) {
            program = programRepository.findByTaskId(taskId);
        } else {
            program = programRepository.findById(catalogId);
        }
        if (program.isPresent()) {
            return R.success(program);
        } else {
            return R.error("数据不存在");
        }
    }
}
