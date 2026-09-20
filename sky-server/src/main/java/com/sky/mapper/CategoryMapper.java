package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
* @author DELL
* @description 针对表【category(菜品及套餐分类)】的数据库操作Mapper
* @createDate 2026-09-20 14:23:29
* @Entity com.sky.entity.Category
*/
@Mapper
public interface CategoryMapper {

    /**
     * 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);
}




