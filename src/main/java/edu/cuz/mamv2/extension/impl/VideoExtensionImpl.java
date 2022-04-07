package edu.cuz.mamv2.extension.impl;

import edu.cuz.mamv2.extension.VideoExtenison;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author VM
 * @date 2022/4/7 21:53
 */
@Component
public class VideoExtensionImpl implements VideoExtenison {
    private static ConcurrentHashMap<String, File> videoTask = new ConcurrentHashMap<>();

    @Override
    public void handleVideo(String videoInfoId, File target) {
        videoTask.put(videoInfoId, target);

    }


    /**
     * 消费任务
     */
    private void consumer() {
        if (!videoTask.isEmpty()){

        }
    }
}
