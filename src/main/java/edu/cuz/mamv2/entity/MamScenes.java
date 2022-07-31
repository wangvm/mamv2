package edu.cuz.mamv2.entity;

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
public class MamScenes {
    public MamScenes() {
        menu = new Menu();
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
    private String title;
    /**
     * 描述
     */
    @Field(index = true, analyzer = "ik_max_word")
    private String description;
    /**
     * 字母形式
     */
    @Field(index = false)
    private String subtitleForm;
    /**
     * 入点
     */
    @Field(index = false)
    private Long startPoint;
    /**
     * 出点
     */
    @Field(index = false)
    private Long outPoint;
    /**
     * 关键帧, value：关键帧图片地址
     */
    @Field(index = false)
    private List<Frame> keyFrames;
}
