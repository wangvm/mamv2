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
            // ??????????????????
            String originalFilename = uploadVideo.getOriginalFilename();
            VideoDTO dto = videoRepository.findByFileName(originalFilename);
            if (dto != null) {
                return R.error("???????????????");
            }
            // ????????????????????????
            String destination = getFilename(videoStoredPath);
            File target = new File(destination);
            try {
                // ????????????
                uploadVideo.transferTo(target);
                // ???????????????????????????????????????
                target.setReadable(true, false);
            } catch (IOException e) {
                log.info("?????????????????????{}", e.getMessage());
                return R.error("?????????????????????");
            }
            // ?????????????????????????????????es???
            String videoInfoId = extractVideoInformation(target, originalFilename);
            // ?????????????????????????????????????????????????????????????????????????????????????????????????????????
            videoExtenison.handleVideo(videoInfoId, target);
            return R.success("??????????????????");
        } else {
            return R.error("????????????");
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
            log.info("???????????????{}", e.getMessage());
            return R.error("????????????");
        }
        return R.success(serverpath + "resource/images/" + filename.substring(filename.length() - 36));
    }

    @Override
    public R getVideoInfo(String taskId) {
        MamTask mamTask = taskMapper.selectById(taskId);
        String videoInfoId = mamTask.getVideoInfoId();
        Optional<VideoDTO> videoDTO = videoRepository.findById(videoInfoId);
        if (!videoDTO.isPresent()) {
            return R.error("???????????????");
        }
        return R.success(videoDTO);
    }

    @Override
    public R getVideoList(Integer pageSize, Integer pageIndex) {
        // ???es?????????????????????
        Page<VideoDTO> page = videoRepository.findAll(PageRequest.of(pageIndex, pageSize));
        return R.success(page);
    }

    @Override
    public R searchVideoByName(String filename, Integer pageIndex, Integer pageSize) {
        // ??????es???match??????????????????????????????????????????5???
        // ??????????????????
        SearchSourceBuilder builder = new SearchSourceBuilder();
        // ??????????????????
        QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("fileName", filename)
                .analyzer("ik_smart")
                .operator(Operator.OR));
        builder.query(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("fileName", filename)
                .analyzer("ik_smart")
                .operator(Operator.OR)))
                // .fetchSource(new String[]{"id", "fileName", "address"}, null)
                // ??????
                .from(pageIndex).size(pageSize)
                // ??????
                .sort("_score", SortOrder.DESC);
        // ??????????????????
        SearchRequest request = new SearchRequest("videoinfo");
        request.source(builder);
        // ??????????????????
        SearchResponse response;
        try {
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.info(e.getMessage());
            return R.error("?????????????????????");
        }
        log.info("??????????????????{}????????????????????????{}??????????????????{}", filename,
                response.getHits().getTotalHits(),
                response.getHits().getMaxScore());
        // ??????????????????
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
            throw new CustomException(HttpStatus.BAD_REQUEST, "????????????????????????");
        }
        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setAddress(serverpath + "resource/video/" + target.getName());
        videoDTO.setFileName(filename);
        VideoSize size = info.getVideo().getSize();
        videoDTO.setAspectRatio(new edu.cuz.mamv2.entity.dto.VideoSize(size.getHeight(), size.getWidth()));
        videoDTO.setFrameRate(info.getVideo().getFrameRate());
        videoDTO.setDuration(info.getDuration());
        videoDTO.setAudioChannel(info.getAudio().getChannels());
        // ????????????????????????es???
        VideoDTO save = videoRepository.save(videoDTO);
        return save.getId();
    }
}
