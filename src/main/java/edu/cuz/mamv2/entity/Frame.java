package edu.cuz.mamv2.entity;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author VM
 * @date 2022/4/11 21:02
 */
@Data
public class Frame {
    @Field(type = FieldType.Text)
    private String address;
    @Field(type = FieldType.Text)
    private String description;
}
