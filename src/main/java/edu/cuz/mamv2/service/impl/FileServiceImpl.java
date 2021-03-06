package edu.cuz.mamv2.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import edu.cuz.mamv2.entity.MamTask;
import edu.cuz.mamv2.entity.dto.VideoDTO;
import edu.cuz.mamv2.extension.VideoExtenison;
import edu.cuz.mamv2.mapper.TaskMapper;
import edu.cuz.mamv2.repository.VideoRepository;
import edu.cuz.mamv2.service.FileService;
import edu.cuz.mamv2.utils.CustomException;
import edu.cuz.mamv2.utils.HttpStatus;
import edu.cuz.mamv2.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.ScreenExtractor;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.info.VideoSize;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

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
    @Resource
    private TaskMapper taskMapper;

    @Override
    public R uploadVideo(MultipartFile uploadVideo) {
        if (CONTENTTYPE.equals(uploadVideo.getContentType())) {
            // 视频源文件名
            String originalFilename = uploadVideo.getOriginalFilename();
            VideoDTO dto = videoRepository.findByFileName(originalFilename);
            if (dto != null) {
                return R.error("视频已存在");
            }
            // 视频随机名称路径
            String destination = getFilename(videoStoredPath);
            File target = new File(destination);
            try {
                // 保存视频
                uploadVideo.transferTo(target);
                // 设置上传视频的读取权限问题
                target.setReadable(true, false);
            } catch (IOException e) {
                log.info("文件保存失败：{}", e.getMessage());
                return R.error("上传失败请重试");
            }
            // 提取视频元信息并保存到es中
            String videoInfoId = extractVideoInformation(target, originalFilename);
            // 对视频的扩展操作，计划实现异步执行提取音频并上传到云服务器提取音频文字
            videoExtenison.handleVideo(videoInfoId, target);
            return R.success("上传文件成功");
        } else {
            return R.error("格式错误");
        }
    }

    @Override
    public R keyFrameCut(Long cutTime, String videoUrl) {
        String videName = videoUrl.substring(videoUrl.length() - 36);
        File video = new File(videoStoredPath + videName);
        MultimediaObject multimediaObject = new MultimediaObject(video);
        ScreenExtractor extractor = new ScreenExtractor();
        String filename = getFilename(imageStoredPath).replace(".mp4", ".png");
        File target = new File(filename);
        try {
            extractor.renderOneImage(multimediaObject, -1, -1, cutTime, target, 1, true);
            target.setReadable(true, false);
        } catch (EncoderException e) {
            log.info("截图失败：{}", e.getMessage());
            return R.error("截图失败");
        }
        return R.success(serverpath + "resource/images/" + filename.substring(filename.length() - 36));
    }

    @Override
    public R getVideoInfo(String taskId) {
        MamTask mamTask = taskMapper.selectById(taskId);
        String videoInfoId = mamTask.getVideoInfoId();
        Optional<VideoDTO> videoDTO = videoRepository.findById(videoInfoId);
        if (!videoDTO.isPresent()) {
            return R.error("视频不存在");
        }
        return R.success(videoDTO);
    }

    @Override
    public R getVideoList(Integer pageSize, Integer pageIndex) {
        // 从es中查询视频列表
        Page<VideoDTO> page = videoRepository.findAll(PageRequest.of(pageIndex, pageSize));
        return R.success(page);
    }

    @Override
    public R searchVideoByName(String filename, Integer pageIndex, Integer pageSize) {
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
                // .fetchSource(new String[]{"id", "fileName", "address"}, null)
                // 分页
                .from(pageIndex).size(pageSize)
                // 排序
                .sort("_score", SortOrder.DESC);
        // 构建查询请求
        SearchRequest request = new SearchRequest("videoinfo");
        request.source(builder);
        // 获取查询结果
        SearchResponse response;
        try {
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.info(e.getMessage());
            return R.error("查询失败请重试");
        }
        log.info("检索关键词：{}，检索文档总数：{}，最大得分：{}", filename,
                response.getHits().getTotalHits(),
                response.getHits().getMaxScore());
        // 组装查询结果
        ArrayList<VideoDTO> videos = new ArrayList<>();
        for (SearchHit hit: response.getHits().getHits()) {
            VideoDTO videoDTO = JSONObject.parseObject(hit.getSourceAsString(), VideoDTO.class);
            videoDTO.setId(hit.getId());
            videos.add(videoDTO);
        }
        return R.success(videos);
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
            throw new CustomException(HttpStatus.BAD_REQUEST, "获取视频信息失败");
        }
        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setAddress(serverpath + "resource/video/" + target.getName());
        videoDTO.setFileName(filename);
        VideoSize size = info.getVideo().getSize();
        videoDTO.setAspectRatio(new edu.cuz.mamv2.entity.dto.VideoSize(size.getHeight(), size.getWidth()));
        videoDTO.setFrameRate(info.getVideo().getFrameRate());
        videoDTO.setDuration(info.getDuration());
        videoDTO.setAudioChannel(info.getAudio().getChannels());
        // 保存视频信息到到es中
        VideoDTO save = videoRepository.save(videoDTO);
        return save.getId();
    }
}
