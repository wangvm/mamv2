package edu.cuz.mamv2.service.impl;

import edu.cuz.mamv2.entity.dto.VideoDTO;
import edu.cuz.mamv2.repository.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.EncodingAttributes;

import javax.annotation.Resource;
import java.io.File;

/**
 * 异步任务提取视频字幕
 * @author VM
 * @date 2022/3/5 20:57
 */
@Slf4j
@Service
public class AsyncVideoService {
    @Resource
    private VideoRepository videoRepository;

    /**
     * 提取视频字幕
     * @param videoInfoId
     * @param target
     */
    @Async
    public void extractVideoSubtitles(String videoInfoId, File target) {
        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setId(videoInfoId);
        // 提取音频
        File audio = new File("E:/Video/target/video/temp.pcm");
        EncodingAttributes attributes = new EncodingAttributes();
        attributes.setOutputFormat("pcm_s16le");
        Encoder encoder = new Encoder();
        try {
            encoder.encode(new MultimediaObject(target), audio, attributes);
        } catch (EncoderException e) {
            e.printStackTrace();
            log.info("提取音频失败");
        }
        // 将音频流上传到讯飞语音转写，留下空挡，以后设计实现

        // 获取提取结果
        Object o = new Object();
        videoDTO.setSubtitle(o);
        videoRepository.save(videoDTO);
    }
}
