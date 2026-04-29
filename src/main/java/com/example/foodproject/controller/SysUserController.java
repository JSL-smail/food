package com.example.foodproject.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.foodproject.common.Result;
import com.example.foodproject.entity.SysUser;
import com.example.foodproject.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys/user")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService sysUserService;

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('sys:user:view')")
    public Result<Page<SysUser>> page(@RequestParam(defaultValue = "1") Integer current,
                                     @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(sysUserService.page(new Page<>(current, size)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:user:view')")
    public Result<SysUser> getById(@PathVariable Long id) {
        return Result.success(sysUserService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('sys:user:add')")
    public Result<String> add(@RequestBody SysUser user) {
        sysUserService.save(user);
        return Result.success("保存成功");
    }

    @PutMapping
    @PreAuthorize("hasAuthority('sys:user:edit')")
    public Result<String> update(@RequestBody SysUser user) {
        sysUserService.updateById(user);
        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    public Result<String> delete(@PathVariable Long id) {
        sysUserService.removeById(id);
        return Result.success("删除成功");
    }
}
