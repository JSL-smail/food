package com.example.foodproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.foodproject.entity.Food;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FoodMapper extends BaseMapper<Food> {
}
