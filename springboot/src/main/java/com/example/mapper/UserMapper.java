package com.example.mapper;

import com.example.entity.User;
import java.util.List;

public interface UserMapper {

    int insert(User user);

    void updateById(User user);

    void deleteById(Integer id);

    User selectById(Integer id);

    User selectByUsername(String username);

    List<User> selectAll(User user);

    int countAll();
}