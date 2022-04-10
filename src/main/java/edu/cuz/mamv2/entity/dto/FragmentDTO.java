package edu.cuz.mamv2.entity.dto;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 片段层实体
 * @author VM
 * @date 2022/3/4 16:26
 */
@Data
@Document(indexName = "fragment", createIndex = false)
public class FragmentDTO {
    public FragmentDTO() {
        menu = new MenuDTO();
        title = new Attributes();
        description = new Attributes();
        creator = new Attributes();
        contributor = new Attributes();
        subtitleForm = new Attributes();
        startPoint = new Attributes();
        outPoint = new Attributes();
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
     * 资料获取方式
     */
    @Field(index = false)
    private Attributes sourceAcquiringMethod;
    /**
     * 提供者
     */
    @Field(index = false)
    private Attributes sourceProvider;
    /**
     * 提取的字幕
     */
    @Field(type = FieldType.Text, index = false)
    private String subtitle;
}
