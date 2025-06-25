package com.example.utils;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.common.Constants;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.service.AdminService;
import com.example.service.DoctorService;
import com.example.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

/*Token工具类 用于处理JWT相关的操作*/
@Component
public class TokenUtils {
    //创建了一个日志记录器，用于记录日志信息
    private static final Logger log = LoggerFactory.getLogger(TokenUtils.class);

    //这些服务用于根据用户角色查询用户信息
    @Resource
    private AdminService adminService;
    @Resource
    DoctorService doctorService;
    @Resource
    UserService userService;

    //定义了三个静态的服务变量，用于在静态方法中访问服务层
    private static AdminService staticAdminService;
    private static DoctorService staticDoctorService;
    private static UserService staticUserService;

    //使用@PostConstruct注解，确保在初始化后立即执行此方法，将服务层注入到静态变量中
    @PostConstruct
    public void init() {
        staticAdminService = adminService;
        staticDoctorService = doctorService;
        staticUserService = userService;
    }

    //这是一个静态方法，用于生成JWT Token
    // 它接受用户ID和角色信息作为data，以及用于签名的密钥sign
    public static String createToken(String data, String sign) {
        // audience是存储数据的一个媒介  存储用户ID和用户的角色
        return JWT.create().withAudience(data)
                .withExpiresAt(DateUtil.offsetDay(new Date(), 1)) // 设置过期时间1天后
                .sign(Algorithm.HMAC256(sign));
    }

    //从当前请求中获取JWT Token，并解析出当前登录的用户信息
    public static Account getCurrentUser() {
        try {
            //从当前请求中获取HTTP请求对象，并从请求头中获取Token
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader(Constants.TOKEN);
            //解析Token，获取存储在audience中的用户ID和角色信息
            String audience = JWT.decode(token).getAudience().get(0);
            String[] userRole = audience.split("-");
            Integer userId = Integer.valueOf(userRole[0]);
            String role = userRole[1];
            //根据用户角色，调用相应的服务层方法查询用户信息。
            if (RoleEnum.ADMIN.name().equals(role)) {
                return staticAdminService.selectById(userId);
            } else if (RoleEnum.DOCTOR.name().equals(role)) {
                return staticDoctorService.selectById(userId);
            } else if (RoleEnum.USER.name().equals(role)) {
                return staticUserService.selectById(userId);
            }
        } catch (Exception e) {
            log.error("获取当前登录用户出错", e);
        }
        return null;
    }

}