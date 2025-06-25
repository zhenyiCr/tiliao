package com.example.controller;

import com.example.common.Result;
import com.example.entity.Science;
import com.example.service.ScienceService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*前端请求接口*/
@RestController
@RequestMapping("/science")
public class ScienceController {

    @Resource
    private ScienceService scienceService;

    /*新增*/
    @PostMapping("/add")
    public Result add(@RequestBody Science science) {
        scienceService.add(science);
        return Result.success();
    }

    /*修改*/
    @PutMapping("/update")
    public Result update(@RequestBody Science science) {
        scienceService.updateById(science);
        return Result.success();
    }

    /*单个删除*/
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        scienceService.deleteById(id);
        return Result.success();
    }

    /*批量删除*/
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        scienceService.deleteBatch(ids);
        return Result.success();
    }

    /*单个查询*/
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Science science = scienceService.selectById(id);
        return Result.success(science);
    }

    /*查询所有*/
    @GetMapping("/selectAll")
    public Result selectAll(Science science) {
        List<Science> list = scienceService.selectAll(science);
        return Result.success(list);
    }

    /*分页查询*/
    @GetMapping("/selectPage")
    public Result selectPage(Science science,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Science> pageInfo = scienceService.selectPage(science, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
