package edu.cuz.mamv2.entity.dto;

import edu.cuz.mamv2.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author VM
 * @date 2022/3/10 21:37
 */
@Data
public class MenuVO extends Menu{

    /**
     * 编目数据id
     */
    private String catalogId;
}
