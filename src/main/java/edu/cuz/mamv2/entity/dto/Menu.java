package edu.cuz.mamv2.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author VM
 * @date 2022/2/22 20:37
 * @description 编目数据菜单
 */
@Data
public class Menu implements Serializable {
    /**
     * 菜单id
     */
    private Long id;
    /**
     * 菜单内容
     */
    private String content;
    /**
     * 菜单层级，节目-片段-场景
     */
    private String level;
    /**
     * 菜单对应编目数据是否通过，-1：不通过，0：待审核，1：通过
     */
    private Integer check;
    /**
     * 父菜单，节目为-1，场景不允许有子菜单
     */
    private Long parent;
}
