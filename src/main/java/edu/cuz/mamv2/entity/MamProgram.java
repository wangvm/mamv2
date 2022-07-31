package edu.cuz.mamv2.entity;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 节目层数据实体
 * @author VM
 * @date 2022/2/22 20:36
 */
@Data
@Document(indexName = "program", createIndex = false)
public class MamProgram implements Serializable {
    public MamProgram() {
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
     * 正题名
     */
    @Field(index = true, analyzer = "ik_max_word", store = true)
    private String title;
    /**
     * 内容描述
     */
    @Field(index = true, analyzer = "ik_max_word")
    private String description;
    /**
     * 首播日期
     */
    @Field(index = false)
    private Date debutDate;
    /**
     * 节目类型
     */
    @Field(index = false)
    private String programType;
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
     * 节目形态
     */
    @Field(index = false)
    private String programForm;
    /**
     * 栏目
     */
    @Field(index = true)
    private String column;
    /**
     * 色彩
     */
    @Field(index = false)
    private String color;
    /**
     * 制式
     */
    @Field(index = false)
    private String system;
    /**
     * 声道格式
     */
    @Field(index = false)
    private String audioChannel;
    /**
     * 画面宽高比
     */
    @Field(index = false)
    private String aspectRatio;
    /**
     * 字幕形式
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
}
