package edu.cuz.mamv2.entity.dto;

import lombok.ToString;
import lombok.experimental.Delegate;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author VM
 * @date 2022/1/19 15:21
 * @description
 */
@ToString
public class ValidationList<E> implements List<E> {
    @Valid
    @Delegate
    public List<E> list = new ArrayList<>();
}
