package edu.cuz.mamv2.controller;

import edu.cuz.mamv2.service.FileService;
import edu.cuz.mamv2.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/upload/video")
    public R uploadVideo(MultipartFile file) {
        R backMessage = videoService.uploadVideo(file);
        return backMessage;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/videoList")
    public R getVideoList(@RequestParam(defaultValue = "5") Integer pageSize,
                          @RequestParam(defaultValue = "1") Integer pageIndex) {
        R videoList = videoService.getVideoList(pageSize, pageIndex - 1);
        return videoList;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public R searchVideoByName(@RequestParam String filename,
                               @RequestParam(defaultValue = "5") Integer pageSize,
                               @RequestParam(defaultValue = "1") Integer pageIndex) {
        R backMessage = videoService.searchVideoByName(filename, pageIndex - 1, pageSize);
        return backMessage;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/videoInfo")
    public R getVideoInfo(String taskId) {
        R backMessage = videoService.getVideoInfo(taskId);
        return backMessage;
    }

    @PreAuthorize("hasRole('CATALOGER')")
    @GetMapping("/keyframe")
    public R keyFrameCut(Long cutTime, String videoUrl) {
        R backMessage = videoService.keyFrameCut(cutTime, videoUrl);
        return backMessage;
    }

    public R removeVideo(String videoName) {
        return null;
    }
}
