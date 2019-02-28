package com.ruoyi.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.demo.domain.Demo;
import com.ruoyi.demo.mapper.DemoMapper;
import com.ruoyi.demo.service.IDemoService;
import com.ruoyi.common.support.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 测试 服务层实现
 * 
 * @author ruoyi
 * @date 2019-01-18
 */
@Service
public class DemoServiceImpl extends ServiceImpl<DemoMapper, Demo> implements IDemoService
{
    @Override
    public List<Demo> selectList(Demo sysDemo) {
        QueryWrapper<Demo> query = new QueryWrapper<>();
        // 查询条件

        return list(query);
    }
	
}
