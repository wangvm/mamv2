package edu.cuz.mamv2.controller;

import edu.cuz.mamv2.service.FileService;
import edu.cuz.mamv2.utils.BackMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author VM
 * @date 2022/3/5 16:55
 */
@RestController
@Slf4j
@RequestMapping("/file")
public class FileController {
    @Resource
    private FileService videoService;

    @PostMapping("/upload/video")
    public BackMessage uploadVideo(MultipartFile file) {
        BackMessage backMessage = videoService.uploadVideo(file);
        return backMessage;
    }

    @GetMapping("/videoList")
    public BackMessage getVideoList(@RequestParam(defaultValue = "5") Integer pageSize,
                                    @RequestParam(defaultValue = "1") Integer pageIndex) {
        BackMessage videoList = videoService.getVideoList(pageSize, pageIndex - 1);
        return videoList;
    }

    @GetMapping("/search")
    public BackMessage searchVideoByName(@RequestParam String filename,
                                         @RequestParam(defaultValue = "5") Integer pageSize,
                                         @RequestParam(defaultValue = "1") Integer pageIndex) {
        BackMessage backMessage = videoService.searchVideoByName(filename, pageIndex - 1, pageSize);
        return backMessage;
    }

    @PostMapping("/upload/keyframe")
    public BackMessage uploadKeyFrame(MultipartFile file) {
        BackMessage backMessage = videoService.uploadKeyFrame(file);
        return backMessage;
    }

    // todo 删除视频接口
    public BackMessage removeVideo(String videoName){
        return null;
    }
}
