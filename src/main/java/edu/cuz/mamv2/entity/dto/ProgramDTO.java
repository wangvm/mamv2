package edu.cuz.mamv2.entity.dto;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 节目层数据实体
 * @author VM
 * @date 2022/2/22 20:36
 */
@Data
@Document(indexName = "program", createIndex = false)
public class ProgramDTO implements Serializable {
    public ProgramDTO() {
        menu = new MenuDTO();
        title = new Attributes();
        description = new Attributes();
        debutDate = new Attributes();
        programType = new Attributes();
        creator = new Attributes();
        contributor = new Attributes();
        programForm = new Attributes();
        column = new Attributes();
        color = new Attributes();
        system = new Attributes();
        audioChannel = new Attributes();
        aspectRatio = new Attributes();
        subtitleForm = new Attributes();
        startPoint = new Attributes();
        outPoint = new Attributes();
        keyFrames = new ArrayList<Frame>();
        sourceAcquiringMethod = new Attributes();
        sourceProvider = new Attributes();
    }

    /**
     * 数据id
     */
    private String id;
    /**
     * 菜单实体
     */
    @Field(store = true)
    private MenuDTO menu;
    /**
     * 编目id
     */
    @Field(index = false)
    private Long taskId;
    /**
     * 正题名
     */
    @Field(index = true, analyzer = "ik_max_word", store = true)
    private Attributes title;
    /**
     * 内容描述
     */
    @Field(index = true, analyzer = "ik_max_word")
    private Attributes description;
    /**
     * 首播日期
     */
    @Field(index = false)
    private Attributes debutDate;
    /**
     * 节目类型
     */
    @Field(index = false)
    private Attributes programType;
    /**
     * 创建者
     */
    @Field(index = false)
    private Attributes creator;
    /**
     * 其他责任者
     */
    @Field(index = false)
    private Attributes contributor;
    /**
     * 节目形态
     */
    @Field(index = false)
    private Attributes programForm;
    /**
     * 栏目
     */
    @Field(index = true)
    private Attributes column;
    /**
     * 色彩
     */
    @Field(index = false)
    private Attributes color;
    /**
     * 制式
     */
    @Field(index = false)
    private Attributes system;
    /**
     * 声道格式
     */
    @Field(index = false)
    private Attributes audioChannel;
    /**
     * 画面宽高比
     */
    @Field(index = false)
    private Attributes aspectRatio;
    /**
     * 字幕形式
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
    /**
     * 资料获取方式
     */
    @Field(index = false)
    private Attributes sourceAcquiringMethod;
    /**
     * 提供者
     */
    @Field(index = false)
    private Attributes sourceProvider;
}
