package com.example.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.example.common.Result;
import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.example.mapper.ExaminationOrderMapper;
import com.example.mapper.TitleMapper;
import com.example.service.*;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class WebController {

    @Resource
    private AdminService adminService;
    @Resource
    private UserService userService;
    @Resource
    private DoctorService doctorService;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Resource
    ExaminationOrderMapper examinationOrderMapper;
    @Resource
    TitleService titleService;
    @Resource
    DepartmentService departmentService;


    /*默认请求接口*/
    @GetMapping("/")
    public Result hello () {
        return Result.success();
    }

    /*登录*/
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account loginAccount = null;
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            loginAccount = adminService.login(account);
        } else if (RoleEnum.USER.name().equals(account.getRole())) {
            loginAccount = userService.login(account);
        } else if (RoleEnum.DOCTOR.name().equals(account.getRole())) {
            loginAccount = doctorService.login(account);
        } else {
            throw new CustomException("500", "非法请求");
        }
        if (loginAccount != null && passwordEncoder.matches(account.getPassword(), loginAccount.getPassword())) {
            return Result.success(loginAccount);
        } else {
            return Result.error("账号或密码错误");
        }
    }

    /*注册*/
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        userService.add(user);
        return Result.success();
    }

    /*修改密码 */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            adminService.updatePassword(account);
        } else if (RoleEnum.DOCTOR.name().equals(account.getRole())) {
            doctorService.updatePassword(account);
        } else if (RoleEnum.USER.name().equals(account.getRole())) {
            userService.updatePassword(account);
        }
        return Result.success();
    }

    /*折线图*/
    @GetMapping("/lineData")
    public Result getLineData() {
        List<ExaminationOrder> examinationOrders = examinationOrderMapper.selectAll(null);
        // 获取到一个月的日期
        Date date = new Date();  // 今天
        DateTime start = DateUtil.offsetDay(date, -30);  // 30天之前
        List<DateTime> dateTimeList = DateUtil.rangeToList(start, date, DateField.DAY_OF_YEAR);
        List<String> dateStrList = dateTimeList.stream().map(DateUtil::formatDate).sorted(Comparator.naturalOrder()).collect(Collectors.toList());
        ArrayList<Integer> moneyList = new ArrayList<>();
        for (String day : dateStrList) {
            Integer money = examinationOrders.stream().filter(o -> o.getCreateTime().contains(day))
                    .filter(o -> o.getStatus().equals("待检查") || o.getStatus().equals("待上传报告") || o.getStatus().equals("已完成"))
                    .map(ExaminationOrder::getMoney).reduce(Integer::sum).orElse(0);
            moneyList.add(money);
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("dateList", dateStrList);
        map.put("moneyList", moneyList);
        return Result.success(map);
    }

    /*饼图*/
    @GetMapping("/pieData")
    public Result getPieData() {
        List<Title> titleList = titleService.selectAll(null);
        List<Map<String, Object>> list = new ArrayList<>();
        for (Title title : titleList) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", title.getName());
            Integer count = doctorService.selectByTitleId(title.getId());
            map.put("value", count);
            list.add(map);
        }
        return Result.success(list);
    }

    /*柱状图*/
    @GetMapping("/barData")
    public Result getDarData() {
        List<Department> departmentList = departmentService.selectAll(null);
        List<String> departmentNames = departmentList.stream().map(Department::getName).collect(Collectors.toList());
        ArrayList<Integer> list = new ArrayList<>();
        for (Department department : departmentList) {
            Integer count = doctorService.selectByDepartmentId(department.getId());
            list.add(count);
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("departmentList", departmentNames);
        map.put("countList", list);
        return Result.success(map);
    }
}