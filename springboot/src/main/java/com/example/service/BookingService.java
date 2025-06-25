package com.example.service;

import com.example.entity.Booking;
import com.example.mapper.BookingMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/*业务层方法*/
@Service
public class BookingService {

    @Resource
    private BookingMapper bookingMapper;

    public void add(Booking booking) {
        bookingMapper.insert(booking);
    }

    public void updateById(Booking booking) {
        bookingMapper.updateById(booking);
    }

    public void deleteById(Integer id) {
        bookingMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            bookingMapper.deleteById(id);
        }
    }

    public Booking selectById(Integer id) {
        return bookingMapper.selectById(id);
    }

    public List<Booking> selectAll(Booking booking) {
        return bookingMapper.selectAll(booking);
    }

    public PageInfo<Booking> selectPage(Booking booking, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Booking> list = bookingMapper.selectAll(booking);
        return PageInfo.of(list);
    }

    public PageInfo<Booking> selectByDoctorName(String name, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Booking> list = bookingMapper.selectByDoctorName(name);
        return new PageInfo<>(list);
    }

}