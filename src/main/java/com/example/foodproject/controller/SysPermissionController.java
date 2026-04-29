package com.example.foodproject.controller;

import com.example.foodproject.common.Result;
import com.example.foodproject.entity.SysPermission;
import com.example.foodproject.service.SysPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys/permission")
@RequiredArgsConstructor
public class SysPermissionController {

    private final SysPermissionService sysPermissionService;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:permission:view')")
    public Result<List<SysPermission>> list() {
        return Result.success(sysPermissionService.list());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:permission:view')")
    public Result<SysPermission> getById(@PathVariable Long id) {
        return Result.success(sysPermissionService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('sys:permission:add')")
    public Result<String> add(@RequestBody SysPermission permission) {
        sysPermissionService.save(permission);
        return Result.success("保存成功");
    }

    @PutMapping
    @PreAuthorize("hasAuthority('sys:permission:edit')")
    public Result<String> update(@RequestBody SysPermission permission) {
        sysPermissionService.updateById(permission);
        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:permission:delete')")
    public Result<String> delete(@PathVariable Long id) {
        sysPermissionService.removeById(id);
        return Result.success("删除成功");
    }
}
