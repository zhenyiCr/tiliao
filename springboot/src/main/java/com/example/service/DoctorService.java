package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Doctor;
import com.example.exception.CustomException;
import com.example.mapper.DoctorMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/*业务层方法*/
@Service
public class DoctorService {

    @Resource
    private DoctorMapper doctorMapper;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void add(Doctor doctor) {
        Doctor dbDoctor = doctorMapper.selectByUsername(doctor.getUsername());
        if (ObjectUtil.isNotNull(dbDoctor)) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(doctor.getPassword())) {
            doctor.setPassword(Constants.USER_DEFAULT_PASSWORD);
        }
        if (ObjectUtil.isEmpty(doctor.getName())) {
            doctor.setName(doctor.getUsername());
        }
        doctor.setRole(RoleEnum.DOCTOR.name());
        doctorMapper.insert(doctor);//当管理员新增一个医生的时候 默认的医生Doctor的身份
    }

    public void updateById(Doctor doctor) {
        doctorMapper.updateById(doctor);
    }

    public void deleteById(Integer id) {
        doctorMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            doctorMapper.deleteById(id);
        }
    }

    public Doctor selectById(Integer id) {
        return doctorMapper.selectById(id);
    }

    public List<Doctor> selectAll(Doctor doctor) {
        return doctorMapper.selectAll(doctor);
    }

    public PageInfo<Doctor> selectPage(Doctor doctor, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Doctor> list = doctorMapper.selectAll(doctor);
        return PageInfo.of(list);
    }

    /**
     * 登录
     */
    public Doctor login(Account account) {
        Doctor dbDoctor = doctorMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbDoctor)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        // 检查数据库中的密码是否已经加密
        if (!passwordEncoder.matches(account.getPassword(), dbDoctor.getPassword())) {
            // 如果密码是明文的，进行加密并更新
            if (dbDoctor.getPassword().equals(account.getPassword())) {
                String encryptedPassword = passwordEncoder.encode(account.getPassword());
                dbDoctor.setPassword(encryptedPassword);
                doctorMapper.updateById(dbDoctor); // 更新为加密后的密码
            } else {
                throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
            }
        }
        // 生成token
        String token = TokenUtils.createToken(dbDoctor.getId() + "-" + dbDoctor.getRole(), dbDoctor.getPassword());
        dbDoctor.setToken(token);
        return dbDoctor;
    }

    /**
     * 修改密码
     */
    public void updatePassword(Account account) {
        Doctor dbDoctor = doctorMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbDoctor)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!passwordEncoder.matches(account.getPassword(), dbDoctor.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        // 加密新密码 
        dbDoctor.setPassword(passwordEncoder.encode(account.getNewPassword()));
        doctorMapper.updateById(dbDoctor);
        dbDoctor.setPassword(account.getNewPassword());
        doctorMapper.updateById(dbDoctor);
    }


    public Integer selectByTitleId(Integer titleId) {
        return doctorMapper.selectByTitleId(titleId);
    }

    public Integer selectByDepartmentId(Integer departmentId) {
        return doctorMapper.selectByDepartmentId(departmentId);
    }
}
