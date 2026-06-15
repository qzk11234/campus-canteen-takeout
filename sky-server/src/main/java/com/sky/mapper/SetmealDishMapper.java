package com.sky.mapper;


import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品id列表查询关联的套餐id列表
     */
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);



}
