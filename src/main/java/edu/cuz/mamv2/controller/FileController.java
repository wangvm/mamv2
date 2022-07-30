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
        return R.toResult(videoService.uploadVideo(file));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/videoList")
    public R getVideoList(@RequestParam(defaultValue = "5") Integer pageSize,
                          @RequestParam(defaultValue = "1") Integer pageIndex) {
        return R.success(videoService.getVideoList(pageSize, pageIndex - 1));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public R searchVideoByName(@RequestParam String filename,
                               @RequestParam(defaultValue = "5") Integer pageSize,
                               @RequestParam(defaultValue = "1") Integer pageIndex) {
        return R.success(videoService.searchVideoByName(filename, pageIndex - 1, pageSize));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/videoInfo")
    public R getVideoInfo(String taskId) {
        return R.success(videoService.getVideoInfo(taskId));
    }

    @PreAuthorize("hasRole('CATALOGER')")
    @GetMapping("/keyframe")
    public R keyFrameCut(Long cutTime, String videoUrl) {
        return R.success(videoService.keyFrameCut(cutTime, videoUrl));
    }

    public R removeVideo(String videoName) {
        return null;
    }
}
