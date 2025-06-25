package com.example.mapper;

import com.example.entity.Title;
import java.util.List;

public interface TitleMapper {

    int insert(Title title);

    void updateById(Title title);

    void deleteById(Integer id);

    Title selectById(Integer id);

    List<Title> selectAll(Title title);
}