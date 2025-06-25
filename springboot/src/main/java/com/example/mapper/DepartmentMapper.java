package com.example.mapper;

import com.example.entity.Department;
import java.util.List;

public interface DepartmentMapper {

    int insert(Department department);

    void updateById(Department department);

    void deleteById(Integer id);

    Department selectById(Integer id);

    List<Department> selectAll(Department department);
}