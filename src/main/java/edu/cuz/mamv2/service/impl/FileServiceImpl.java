package edu.cuz.mamv2.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import edu.cuz.mamv2.entity.dto.VideoDTO;
import edu.cuz.mamv2.extension.VideoExtenison;
import edu.cuz.mamv2.repository.VideoRepository;
import edu.cuz.mamv2.service.FileService;
import edu.cuz.mamv2.utils.BackEnum;
import edu.cuz.mamv2.utils.BackMessage;
import edu.cuz.mamv2.utils.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.info.MultimediaInfo;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author VM
 * @date 2022/3/5 17:05
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {
    private final static String CONTENTTYPE = "video/mp4";
    @Value("${videoStoredPath}")
    private String videoStoredPath;
    @Value("${imageStoredPath}")
    private String imageStoredPath;
    @Value("${serverPath}")
    private String serverpath;
    @Resource
    private VideoExtenison videoExtenison;
    @Resource
    private VideoRepository videoRepository;
    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Override
    public BackMessage uploadVideo(MultipartFile uploadVideo) {
        if (CONTENTTYPE.equals(uploadVideo.getContentType())) {
            // 视频源文件名
            String originalFilename = uploadVideo.getOriginalFilename();
            // 视频随机名称路径
            String destination = getFilename(videoStoredPath);
            File target = new File(destination);
            try {
                // 保存视频
                uploadVideo.transferTo(target);
                // 设置上传视频的读取权限问题
                target.setReadable(true);
            } catch (IOException e) {
                log.info("文件保存失败：{}", e.getMessage());
                return new BackMessage().failureWithMessage("上传失败请重试");
            }
            // 提取视频元信息并保存到es中
            String videoInfoId = extractVideoInformation(target, originalFilename);
            // 对视频的扩展操作，计划实现异步执行提取音频并上传到云服务器提取音频文字
            videoExtenison.handleVideo(videoInfoId, target);
            return new BackMessage().successWithMessage("上传文件成功");
        } else {
            return new BackMessage().failureWithMessage("格式错误");
        }
    }

    @Override
    public BackMessage uploadKeyFrame(MultipartFile keyFrame) {
        // 视频随机名称路径
        String destination = getFilename(imageStoredPath);
        File target = new File(destination);
        try {
            keyFrame.transferTo(target);
            target.setReadable(true);
        } catch (IOException e) {
            log.info("文件保存失败：{}", e.getMessage());
            return new BackMessage().failureWithMessage("上传失败请重试");
        }
        return new BackMessage().successWithMessageAndData("上传文件成功", serverpath + "images/" + target.getName());
    }

    @Override
    public BackMessage getVideoList(Integer pageSize, Integer pageIndex) {
        // 从es中查询视频列表
        Page<VideoDTO> page = videoRepository.findAll(PageRequest.of(pageIndex, pageSize));
        return new BackMessage(BackEnum.SUCCESS, page);
    }

    @Override
    public BackMessage searchVideoByName(String filename, Integer pageIndex, Integer pageSize) {
        // 通过es的match查询关键词返回结果，默认返回5条
        // 构造查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        // 指定匹配条件
        QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("fileName", filename)
                .analyzer("ik_smart")
                .operator(Operator.OR));
        builder.query(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("fileName", filename)
                        .analyzer("ik_smart")
                        .operator(Operator.OR)))
                //
                .fetchSource(new String[]{"id", "fileName", "address"}, null)
                // 分页
                .from(pageIndex).size(pageSize)
                // 排序
                .sort("_score", SortOrder.DESC)
                // 查询结果高亮
                .highlighter(new HighlightBuilder().field("*")
                        .requireFieldMatch(false)
                        .preTags("\"<span style='color:red'>\"")
                        .postTags("\"</span>\""));
        // 构建查询请求
        SearchRequest request = new SearchRequest("videoinfo");
        request.source(builder);
        // 获取查询结果
        SearchResponse response;
        try {
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.info(e.getMessage());
            return new BackMessage().failureWithMessage("查询失败请重试");
        }
        log.info("检索关键词：{}，检索文档总数：{}，最大得分：{}", filename,
                response.getHits().getTotalHits(),
                response.getHits().getMaxScore());
        // 组装查询结果
        ArrayList<VideoDTO> videos = new ArrayList<>();
        for (SearchHit hit: response.getHits().getHits()) {
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            VideoDTO videoDTO = JSONObject.parseObject(hit.getSourceAsString(), VideoDTO.class);
            // 关键词高亮替换
            videoDTO.setFileName(highlightFields.get("fileName").fragments()[0].toString());
            videos.add(videoDTO);
        }
        return new BackMessage(BackEnum.SUCCESS, videos);
    }

    private String getFilename(String prefix) {
        return prefix + IdUtil.simpleUUID() + ".mp4";
    }

    private String extractVideoInformation(File target, String filename) {
        MultimediaObject multimediaObject = new MultimediaObject(target);
        MultimediaInfo info = null;
        try {
            info = multimediaObject.getInfo();
        } catch (EncoderException e) {
            throw new CustomException("获取视频信息失败", 400);
        }
        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setAddress(serverpath + "vod/" + target.getName());
        videoDTO.setFileName(filename);
        videoDTO.setAspectRatio(info.getVideo().getSize());
        videoDTO.setFrameRate(info.getVideo().getFrameRate());
        videoDTO.setDuration(info.getDuration());
        videoDTO.setAudioChannel(info.getAudio().getChannels());
        // 保存视频信息到到es中
        VideoDTO save = videoRepository.save(videoDTO);
        return save.getId();
    }
}
