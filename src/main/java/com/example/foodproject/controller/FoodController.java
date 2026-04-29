package com.example.foodproject.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.foodproject.common.Result;
import com.example.foodproject.entity.Food;
import com.example.foodproject.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/food")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @GetMapping("/page")
    public Result<Page<Food>> page(@RequestParam(defaultValue = "1") Integer current,
                                  @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(foodService.page(new Page<>(current, size)));
    }

    @GetMapping("/{id}")
    public Result<Food> getById(@PathVariable Long id) {
        return Result.success(foodService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('food:add')")
    public Result<String> add(@RequestBody Food food) {
        foodService.save(food);
        return Result.success("保存成功");
    }

    @PutMapping
    @PreAuthorize("hasAuthority('food:edit')")
    public Result<String> update(@RequestBody Food food) {
        foodService.updateById(food);
        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('food:delete')")
    public Result<String> delete(@PathVariable Long id) {
        foodService.removeById(id);
        return Result.success("删除成功");
    }
}
