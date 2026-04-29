package com.example.foodproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.foodproject.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
