package edu.cuz.mamv2.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author VM
 * @date 2022/4/14 23:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoSize {
    private static final long serialVersionUID = 1L;
    private Integer width;
    private Integer height;

    public String asEncoderArgument() {
        return getWidth() + "x" + getHeight();
    }
}
