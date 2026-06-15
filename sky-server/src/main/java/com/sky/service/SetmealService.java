package com.sky.service;


import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    /**
     * 新增套餐并保存套餐菜品关系
     */
    void saveWithDish(SetmealDTO setmealDTO);

    /**
     * 分页查询套餐
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 批量删除套餐
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询套餐详情，包含套餐菜品关系
     */
    SetmealVO getByIdWithDish(Long id);

    /**
     * 更新套餐并保存套餐菜品关系
     */
    void updateWithDish(SetmealDTO setmealDTO);

    /**
     * 套餐起售、停售
     */
    void startOrStop(Integer status, Long id);

}
