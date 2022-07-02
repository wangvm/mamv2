package edu.cuz.mamv2.service;

import edu.cuz.mamv2.utils.R;
import org.springframework.web.multipart.MultipartFile;

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
    R uploadVideo(MultipartFile uploadVideo);


    /**
     * 查询视频列表
     * @param pageSize 分页大小
     * @param pageIndex 当前页
     * @return
     */
    R getVideoList(Integer pageSize, Integer pageIndex);

    /**
     * 通过关键字在es中查询视频列表，默认返回5条
     * @param filename 查询的视频关键字
     * @param pageIndex
     * @param pageSize
     * @return
     */
    R searchVideoByName(String filename, Integer pageIndex, Integer pageSize);

    /**
     * 上传关键帧
     * @param cutTime 关键帧图片
     * @param videoUrl
     * @return
     */
    R keyFrameCut(Long cutTime, String videoUrl);

    /**
     * 获取视频信息
     * @param taskId 名字
     * @return {@link R}
     */
    R getVideoInfo(String taskId);
}
