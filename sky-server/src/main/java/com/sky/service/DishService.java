package com.sky.service;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
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

    /**
     * 根据菜品id查询菜品详情，包含菜品口味
     */
    DishVO getByIdWithFlavor(Long id);

    /**
     * 更新菜品并保存菜品口味
     */
    void updateWithFlavor(DishDTO dishDTO);

    /**
     * 根据分类id查询菜品列表
     */
    List<Dish> listByCategoryId(Long categoryId);
    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);

    /**
     * 菜品起售、停售
     */
    void startOrStop(Integer status, Long id);
}
