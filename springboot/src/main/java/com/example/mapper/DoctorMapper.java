package com.example.mapper;

import com.example.entity.Doctor;
import java.util.List;

public interface DoctorMapper {

    int insert(Doctor doctor);

    void updateById(Doctor doctor);

    void deleteById(Integer id);

    Doctor selectById(Integer id);

    Doctor selectByUsername(String username);

    List<Doctor> selectAll(Doctor doctor);

    int countAll();

    Integer selectByTitleId(Integer titleId);

    Integer selectByDepartmentId(Integer departmentId);
}