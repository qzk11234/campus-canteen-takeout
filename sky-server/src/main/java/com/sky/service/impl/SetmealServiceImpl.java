package com.sky.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private DishMapper dishMapper;

    /**
     * 新增套餐并保存套餐菜品关系
     */
    @Transactional
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        //新增套餐
        setmealMapper.insert(setmeal);
        //获取新增套餐的id
        Long setmealId = setmeal.getId();
        //新增套餐菜品关系
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && setmealDishes.size() > 0) {
            setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));
            setmealDishMapper.insertBatch(setmealDishes);
        }
    }

    /**
     * 分页查询套餐
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 批量删除套餐
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //判断是否为空
        if (ids == null || ids.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.SETMEAL_DELETE_SELECT_NULL);
        }
        //判断套餐是否能够删除--是否存在起售中的套餐？
        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.selectById(id);
            if (setmeal.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        //删除套餐
        setmealMapper.deleteByIds(ids);
        //删除套餐菜品关系
        setmealDishMapper.deleteBySetmealIds(ids);
    }

    /**
     * 根据id查询套餐详情，包含套餐菜品关系
     */
    @Override
    public SetmealVO getByIdWithDish(Long id) {
        //根据id查询套餐详情
        Setmeal setmeal = setmealMapper.selectById(id);
        //根据套餐id查询套餐菜品关系
        List<SetmealDish> setmealDishes = setmealDishMapper.selectBySetmealId(id);
        //将套餐菜品关系添加到套餐详情中
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    /**
     * 更新套餐并保存套餐菜品关系
     */
    @Transactional
    public void updateWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        //修改套餐基本数据
        setmealMapper.update(setmeal);
        //先删除原先的套餐菜品关系
        setmealDishMapper.deleteBySetmealId(setmealDTO.getId());
        //重新添加套餐菜品关系
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && setmealDishes.size() > 0) {
            setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealDTO.getId()));
            setmealDishMapper.insertBatch(setmealDishes);
        }
    }

    /**
     * 套餐起售、停售
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        //如果是起售操作，需要检查套餐内的菜品是否都在起售状态
        if (status == StatusConstant.ENABLE) {
            List<SetmealDish> setmealDishes = setmealDishMapper.selectBySetmealId(id);
            if (setmealDishes != null && setmealDishes.size() > 0) {
                for (SetmealDish setmealDish : setmealDishes) {
                    Dish dish = dishMapper.selectById(setmealDish.getDishId());
                    if (dish.getStatus() == StatusConstant.DISABLE) {
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                }
            }
        }
        //更新套餐状态
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setmeal);
    }

}
