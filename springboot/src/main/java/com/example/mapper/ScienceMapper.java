package com.example.mapper;

import com.example.entity.Science;
import java.util.List;

public interface ScienceMapper {

    int insert(Science science);

    void updateById(Science science);

    void deleteById(Integer id);

    Science selectById(Integer id);

    List<Science> selectAll(Science science);
}