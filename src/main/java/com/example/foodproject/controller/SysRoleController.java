package com.example.foodproject.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.foodproject.common.Result;
import com.example.foodproject.entity.SysRole;
import com.example.foodproject.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sys/role")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleService sysRoleService;

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('sys:role:view')")
    public Result<Page<SysRole>> page(@RequestParam(defaultValue = "1") Integer current,
                                     @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(sysRoleService.page(new Page<>(current, size)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:role:view')")
    public Result<SysRole> getById(@PathVariable Long id) {
        return Result.success(sysRoleService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('sys:role:add')")
    public Result<String> add(@RequestBody SysRole role) {
        sysRoleService.save(role);
        return Result.success("保存成功");
    }

    @PutMapping
    @PreAuthorize("hasAuthority('sys:role:edit')")
    public Result<String> update(@RequestBody SysRole role) {
        sysRoleService.updateById(role);
        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    public Result<String> delete(@PathVariable Long id) {
        sysRoleService.removeById(id);
        return Result.success("删除成功");
    }
}
