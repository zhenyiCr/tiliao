package com.example.controller;

import com.example.common.Result;
import com.example.entity.Booking;
import com.example.service.BookingService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*前端请求接口*/
@RestController
//一个Spring MVC注解，用于定义类为RESTful控制器
// 它组合了@Controller和@ResponseBody注解
// 意味着该类中所有的方法都会返回一个响应体（通常是JSON格式的数据）。
@RequestMapping("/booking")  //类级别的请求映射，所有的方法都会映射到/booking路径下
public class BookingController {

    @Resource
    private BookingService bookingService;

    /*新增*/
    @PostMapping("/add")
    public Result add(@RequestBody Booking booking) { //接收一个JSON格式的请求体，并将其转换为Booking对象
        bookingService.add(booking);
        return Result.success();
    }

    /*修改*/
    @PutMapping("/update")
    public Result update(@RequestBody Booking booking) {
        bookingService.updateById(booking);
        return Result.success();
    }

    /*单个删除*/
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        bookingService.deleteById(id);
        return Result.success();
    }

    /*批量删除*/
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        bookingService.deleteBatch(ids);
        return Result.success();
    }

    /*单个查询*/
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Booking booking = bookingService.selectById(id);
        return Result.success(booking);
    }

    /*查询所有*/
    @GetMapping("/selectAll")
    public Result selectAll(Booking booking) {
        List<Booking> list = bookingService.selectAll(booking);
        return Result.success(list);
    }

    /*分页查询*/
    @GetMapping("/selectPage")
    public Result selectPage(Booking booking,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Booking> pageInfo = bookingService.selectPage(booking, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    /*根据医生姓名查询*/
    @GetMapping("/selectByDoctorName")
    public Result selectByDoctorName(@RequestParam(value = "name", required = false) String name,
                                     @RequestParam(defaultValue = "1") Integer pageNum,
                                     @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Booking> pageInfo = bookingService.selectByDoctorName(name, pageNum, pageSize);
        return Result.success(pageInfo);
    }
}