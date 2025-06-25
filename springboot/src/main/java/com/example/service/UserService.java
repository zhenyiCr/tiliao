package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/*业务层方法*/
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void add(User user) {
        User dbUser = userMapper.selectByUsername(user.getUsername());
        if (ObjectUtil.isNotNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(user.getPassword())) {
            user.setPassword(Constants.USER_DEFAULT_PASSWORD);
        }
        if (ObjectUtil.isEmpty(user.getName())) {
            user.setName(user.getUsername());
        }
        user.setRole(RoleEnum.USER.name());
        userMapper.insert(user);
    }

    public void updateById(User user) {
        userMapper.updateById(user);
    }

    public void deleteById(Integer id) {
        userMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            userMapper.deleteById(id);
        }
    }

    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }

    public List<User> selectAll(User user) {
        return userMapper.selectAll(user);
    }

    public PageInfo<User> selectPage(User user, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> list = userMapper.selectAll(user);
        return PageInfo.of(list);
    }

    /*登录*/
    public User login(Account account) {
        User dbUser = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        // 检查数据库中的密码是否已经加密
        if (!passwordEncoder.matches(account.getPassword(), dbUser.getPassword())) {
            // 如果密码是明文的，则将其加密并更新
            if (dbUser.getPassword().equals(account.getPassword())) {
                String encryptedPassword = passwordEncoder.encode(account.getPassword());
                dbUser.setPassword(encryptedPassword);
                userMapper.updateById(dbUser); // 更新为加密后的密码
            } else {
                throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
            }
        }
        // 生成token
        String token = TokenUtils.createToken(dbUser.getId() + "-" + dbUser.getRole(), dbUser.getPassword());
        dbUser.setToken(token);
        return dbUser;
    }

    /*修改密码*/
    public void updatePassword(Account account) {
        User dbUser = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        // 使用BCryptPasswordEncoder来比较明文密码和数据库中的加密密码
        if (!passwordEncoder.matches(account.getPassword(), dbUser.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR); // 密码错误
        }
        // 更新为新的加密密码
        String encryptedNewPassword = passwordEncoder.encode(account.getNewPassword());
        dbUser.setPassword(encryptedNewPassword);
        userMapper.updateById(dbUser);
    }


}
