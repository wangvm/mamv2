package edu.cuz.mamv2.entity;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

/**
 * 片段层实体
 * @author VM
 * @date 2022/3/4 16:26
 */
@Data
@Document(indexName = "fragment", createIndex = false)
public class MamFragment {
    public MamFragment() {
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
     * 创建者
     */
    @Field(index = false)
    private String creator;
    /**
     * 其他责任者
     */
    @Field(index = false)
    private String contributor;
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
     * 资料获取方式
     */
    @Field(index = false)
    private String sourceAcquiringMethod;
    /**
     * 提供者
     */
    @Field(index = false)
    private String sourceProvider;
    /**
     * 关键帧, value：关键帧图片地址
     */
    @Field(index = false)
    private List<Frame> keyFrames;
    /**
     * 提取的字幕
     */
    @Field(type = FieldType.Text, index = false)
    private String subtitle;
}
