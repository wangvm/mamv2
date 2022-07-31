package edu.cuz.mamv2.service;

import edu.cuz.mamv2.entity.VideoInfo;
import edu.cuz.mamv2.utils.R;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author VM
 * @date 2022/3/5 17:04
 */
public interface FileService {

    /**
     * 保存上传的视频
     * @param uploadVideo 上传的视频流
     * @return
     */
    String uploadVideo(MultipartFile uploadVideo);

    /**
     * 查询视频列表
     * @param pageSize 分页大小
     * @param pageIndex 当前页
     * @return
     */
    Page<VideoInfo> getVideoList(Integer pageSize, Integer pageIndex);

    /**
     * 通过关键字在es中查询视频列表，默认返回5条
     * @param filename 查询的视频关键字
     * @param pageIndex
     * @param pageSize
     * @return
     */
    List<VideoInfo> searchVideoByName(String filename, Integer pageIndex, Integer pageSize);

    /**
     * 上传关键帧
     * @param cutTime 关键帧图片
     * @param videoUrl
     * @return
     */
    String keyFrameCut(Long cutTime, String videoUrl);

    /**
     * 获取视频信息
     * @param taskId 名字
     * @return {@link R}
     */
    VideoInfo getVideoInfo(String taskId);
}
