package edu.cuz.mamv2.entity.dto;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.util.List;

/**
 * 场景层实体
 * @author VM
 * @date 2022/3/4 16:41
 */
@Data
@Document(indexName = "scenes", createIndex = false)
public class ScenesDTO {
    public ScenesDTO() {
        menu = new Menu();
        title = new Attributes();
        description = new Attributes();
        subtitleForm = new Attributes();
        startPoint = new Attributes();
        outPoint = new Attributes();
    }

    /**
     * 数据id
     */
    private String id;
    /**
     * 菜单实体
     */
    @Field(store = true)
    private Menu menu;
    /**
     * 编目id
     */
    @Field(index = false)
    private Long taskId;
    /**
     * 题名
     */
    @Field(index = true, analyzer = "ik_max_word", store = true)
    private Attributes title;
    /**
     * 描述
     */
    @Field(index = true, analyzer = "ik_max_word")
    private Attributes description;
    /**
     * 字母形式
     */
    @Field(index = false)
    private Attributes subtitleForm;
    /**
     * 入点
     */
    @Field(index = false)
    private Attributes startPoint;
    /**
     * 出点
     */
    @Field(index = false)
    private Attributes outPoint;
    /**
     * 关键帧, value：关键帧图片地址
     */
    @Field(index = false)
    private List<Frame> keyFrames;
}
