package edu.cuz.mamv2.entity.dto;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Mapping;

/**
 * 视频信息实体
 * @author VM
 * @date 2022/3/5 20:47
 */
@Data
@Document(indexName = "videoinfo", createIndex = true)
public class VideoDTO {

    /**
     * 数据id
     */
    private String id;
    /**
     * 视频名称
     */
    @Field(type = FieldType.Text, store = true, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String fileName;
    /**
     * 视频画面宽高比
     */
    @Field(type = FieldType.Nested, index = false)
    private VideoSize aspectRatio;
    /**
     * 视频时长
     */
    @Field(type = FieldType.Long, index = false)
    private Long duration;
    /**
     * 视频帧率
     */
    @Field(type = FieldType.Float, index = false)
    private Float frameRate;
    /**
     * 视频地址
     */
    @Field(type = FieldType.Auto, index = false)
    private String address;
    /**
     * 声道
     */
    @Field(type = FieldType.Integer, index = false)
    private Integer audioChannel;
    /**
     * 字幕
     */
    @Field(type = FieldType.Nested, index = false)
    private Object subtitle;
}
