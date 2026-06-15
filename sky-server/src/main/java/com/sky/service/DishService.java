package com.sky.service;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface DishService {




    /**

     * 新增菜品并保存菜品口味
     */
    public void saveWithFlavor(DishDTO dishDTO);

    /**
     * 分页查询菜品
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);


    /**
     * 批量删除菜品
     */
    void deleteBatch(List<Long> ids);
}
