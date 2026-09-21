package com.sky.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class CategoryDTO implements Serializable {

    //主键
    private Long id;

    @NotBlank(message = "分类类型不能为空")
    //类型 1 菜品分类 2 套餐分类
    private Integer type;

    @NotBlank(message = "分类名称不能为空")
    private String name;

    @NotBlank(message = "排序不能为空")
    private Integer sort;

}
