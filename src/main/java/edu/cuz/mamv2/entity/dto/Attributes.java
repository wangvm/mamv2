package edu.cuz.mamv2.entity.dto;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * @author VM
 * @date 2022/2/22 20:40
 * @description
 */
@Data
public class Attributes implements Serializable {
    /**
     * 属性值
     */
    private String value;
    /**
     * 属性是否审核通过
     */
    @Field(type = FieldType.Integer)
    private Integer check;

    public Attributes(){

    }

    public Attributes(String value){
        this.value = value;
    }
}
