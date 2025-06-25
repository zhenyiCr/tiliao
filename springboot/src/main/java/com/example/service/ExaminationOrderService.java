package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Booking;
import com.example.entity.ExaminationOrder;
import com.example.exception.CustomException;
import com.example.mapper.ExaminationOrderMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/*业务层方法*/
@Service
public class ExaminationOrderService {

    @Resource
    private ExaminationOrderMapper examinationOrderMapper;
    @Resource
    private BookingService bookingService;

    public void add(ExaminationOrder examinationOrder) {
        examinationOrder.setCreateTime(DateUtil.now());
        Account currentUser = TokenUtils.getCurrentUser();

        examinationOrder.setUserId(currentUser.getId());
        examinationOrder.setDoctorId(examinationOrder.getDoctorId());
        examinationOrder.setMoney(examinationOrder.getMoney());


        Date date = new Date();
        String orderId = DateUtil.format(date, "yyyyMMdd") + date.getTime();  // 唯一的订单号
        examinationOrder.setOrderId(orderId);
        examinationOrder.setStatus("待审批");
        examinationOrderMapper.insert(examinationOrder);
    }

    public void updateById(ExaminationOrder examinationOrder) {
        if ("待上传报告".equals(examinationOrder.getStatus())) {
            examinationOrder.setCheckTime(DateUtil.now());
        }
        examinationOrderMapper.updateById(examinationOrder);
    }

    public void deleteById(Integer id) {
        examinationOrderMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            examinationOrderMapper.deleteById(id);
        }
    }

    public ExaminationOrder selectById(Integer id) {
        return examinationOrderMapper.selectById(id);
    }

    public List<ExaminationOrder> selectAll(ExaminationOrder examinationOrder) {
        return examinationOrderMapper.selectAll(examinationOrder);
    }

    public PageInfo<ExaminationOrder> selectPage(ExaminationOrder examinationOrder, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();

        if(RoleEnum.USER.name().equals(currentUser.getRole())){
            examinationOrder.setUserId(currentUser.getId());
        }
        if(RoleEnum.DOCTOR.name().equals(currentUser.getRole())){
            examinationOrder.setDoctorId(currentUser.getId());
        }

        PageHelper.startPage(pageNum, pageSize);
        List<ExaminationOrder> list = examinationOrderMapper.selectAll(examinationOrder);
        return PageInfo.of(list);
    }

}
