package edu.cuz.mamv2.entity.dto;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

/**
 * 场景层实体
 * @author VM
 * @date 2022/3/4 16:41
 */
@Data
@Document(indexName = "scenes", createIndex = false)
public class ScenesDTO {
    public ScenesDTO() {
        this.menu = new MenuDTO();
        this.title = new Attributes();
        this.decription = new Attributes();
        this.subtitleForm = new Attributes();
        this.startPoint = new Attributes();
        this.outPoint = new Attributes();
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
    private Attributes decription;
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
}
