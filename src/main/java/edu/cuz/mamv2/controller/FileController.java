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
@RequestMapping("/video")
public class FileController {
    @Resource
    private FileService videoService;

    @PostMapping("/upload")
    public BackMessage uploadVideo(@RequestBody MultipartFile uploadVideo) {
        BackMessage backMessage = videoService.uploadVideo(uploadVideo);
        return backMessage;
    }

    @GetMapping("/videolist")
    public BackMessage getVideoList(@RequestParam(defaultValue = "10") Integer pageSize,
                                    @RequestParam(defaultValue = "0") Integer pageIndex) {
        BackMessage videoList = videoService.getVideoList(pageSize, pageIndex);
        return videoList;
    }

    @GetMapping("/search")
    public BackMessage searchVideoByName(@RequestParam String filename,
                                         @RequestParam(defaultValue = "5") Integer pageSize,
                                         @RequestParam(defaultValue = "0") Integer pageIndex) {
        BackMessage backMessage = videoService.searchVideoByName(filename, pageIndex, pageSize);
        return backMessage;
    }

    @PostMapping("/upload/keyframe")
    public BackMessage uploadKeyFrame(@RequestBody MultipartFile keyFrame){
        BackMessage backMessage = videoService.uploadKeyFrame(keyFrame);
        return backMessage;
    }
}
