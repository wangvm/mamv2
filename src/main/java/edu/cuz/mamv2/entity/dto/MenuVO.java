package edu.cuz.mamv2.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author VM
 * @date 2022/3/10 21:37
 */
@Data
@AllArgsConstructor
public class MenuVO {
    /**
     * 编目数据id
     */
    private String catalogId;
    private Menu menu;
}
