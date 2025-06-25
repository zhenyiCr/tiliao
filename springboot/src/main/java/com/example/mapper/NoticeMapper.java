package com.example.mapper;

import com.example.entity.Notice;
import java.util.List;

public interface NoticeMapper {

    int insert(Notice notice);

    void updateById(Notice notice);

    void deleteById(Integer id);

    Notice selectById(Integer id);

    List<Notice> selectAll(Notice notice);
}