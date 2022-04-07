package edu.cuz.mamv2.extension;

import org.springframework.scheduling.annotation.Async;

import java.io.File;

/**
 * @author VM
 * @date 2022/4/7 21:55
 */
@Async
public interface VideoExtenison {

    /**
     * 处理视频
     * @param videoInfoId es中的视频信息id
     * @param target 视频文件地址
     */
    public void handleVideo(String videoInfoId, File target);
}
