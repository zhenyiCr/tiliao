package com.example.mapper;

import com.example.entity.ExaminationOrder;
import java.util.List;

public interface ExaminationOrderMapper {

    int insert(ExaminationOrder examinationOrder);

    void updateById(ExaminationOrder examinationOrder);

    void deleteById(Integer id);

    ExaminationOrder selectById(Integer id);

    List<ExaminationOrder> selectAll(ExaminationOrder examinationOrder);
}