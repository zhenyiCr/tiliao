package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.Science;
import com.example.mapper.ScienceMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/*业务层方法*/
@Service
public class ScienceService {

    @Resource
    private ScienceMapper scienceMapper;

    public void add(Science science) {
        Account currentUser = TokenUtils.getCurrentUser();
        science.setAuthorId(currentUser.getId());
        science.setPublishTime(DateUtil.now());
        scienceMapper.insert(science);
    }

    public void updateById(Science science) {
        scienceMapper.updateById(science);
    }

    public void deleteById(Integer id) {
        scienceMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            scienceMapper.deleteById(id);
        }
    }

    public Science selectById(Integer id) {
        return scienceMapper.selectById(id);
    }

    public List<Science> selectAll(Science science) {
        return scienceMapper.selectAll(science);
    }

    public PageInfo<Science> selectPage(Science science, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Science> list = scienceMapper.selectAll(science);
        return PageInfo.of(list);
    }

}
