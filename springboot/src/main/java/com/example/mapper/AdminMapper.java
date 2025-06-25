package com.example.mapper;

import com.example.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface AdminMapper {

    int insert(Admin admin);

    void updateById(Admin admin);

    void deleteById(Integer id);

    Admin selectById(Integer id);

    Admin selectByUsername(String username);

    List<Admin> selectAll(Admin admin);

}