package com.example.mapper;

import com.example.entity.Booking;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookingMapper {

    int insert(Booking booking);

    void updateById(Booking booking);

    void deleteById(Integer id);

    Booking selectById(Integer id);

    List<Booking> selectAll(Booking booking);

    List<Booking> selectByDoctorName(@Param("name") String name);
}