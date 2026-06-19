package com.sky.mapper;



import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {


    /**
     * 动态查询购物车列表
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);


    /**
     * 更新购物车数量
     * @param shoppingCart
     */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumberById(ShoppingCart shoppingCart);


    /**
     * 添加购物车
     * @param shoppingCart
     */
    @Insert("insert into shopping_cart (name,user_id, dish_id, setmeal_id, dish_flavor,number, amount, image, create_time) " +
            "values (#{name}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount}, #{image}, #{createTime})")
    void insert(ShoppingCart shoppingCart);

    /**
     * 根据用户id删除购物车
     * @param userId
     */
    @Delete("delete from shopping_cart where user_id = #{userId}")
    void deleteByUserId(Long userId);

    /**
     * 根据用户id和菜品id和口味删除购物车商品
     * @param userId
     * @param dishId
     * @param dishFlavor
     */

    void deleteByDishId(Long userId, Long dishId, String dishFlavor);

    /**
     * 根据用户id和套餐id删除购物车商品
     * @param userId
     * @param setmealId
     */
    @Delete("delete from shopping_cart where user_id = #{userId} and setmeal_id = #{setmealId}")
    void deleteBySetmealId(Long userId, Long setmealId);
}
