package com.example.foodproject.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.foodproject.common.Result;
import com.example.foodproject.entity.Orders;
import com.example.foodproject.security.SecurityUtils;
import com.example.foodproject.service.OrdersService;
import com.example.foodproject.util.OrderNoGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersService ordersService;

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('orders:view')")
    public Result<Page<Orders>> page(@RequestParam(defaultValue = "1") Integer current,
                                    @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(ordersService.page(new Page<>(current, size)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('orders:view')")
    public Result<Orders> getById(@PathVariable Long id) {
        return Result.success(ordersService.getById(id));
    }

    @PostMapping
    public Result<String> add(@RequestBody Orders orders) {
        Long currentUserId = SecurityUtils.getUserId();
        if (currentUserId == null) {
            return Result.error(401, "请先登录");
        }
        orders.setUserId(currentUserId);
        orders.setOrderNo(OrderNoGenerator.generate());
        
        ordersService.save(orders);
        return Result.success("订单创建成功");
    }

    @PutMapping
    @PreAuthorize("hasAuthority('orders:edit')")
    public Result<String> update(@RequestBody Orders orders) {
        // 也可以在更新时验证订单是否属于当前用户
        ordersService.updateById(orders);
        return Result.success("订单更新成功");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('orders:delete')")
    public Result<String> delete(@PathVariable Long id) {
        ordersService.removeById(id);
        return Result.success("订单删除成功");
    }
}
