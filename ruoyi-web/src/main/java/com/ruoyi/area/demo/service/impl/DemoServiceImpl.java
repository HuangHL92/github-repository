package com.ruoyi.area.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.area.demo.domain.Demo;
import com.ruoyi.area.demo.mapper.DemoMapper;
import com.ruoyi.area.demo.service.IDemoService;
import com.ruoyi.common.utils.StringUtils;
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
    public List<Demo> selectList(Demo demo) {
        QueryWrapper<Demo> query = new QueryWrapper<>();
        // 查询条件
        if(StringUtils.isNotEmpty(demo.getName())) {
            query.lambda()
                    .likeRight(Demo::getName, demo.getName());
            //query.like("name",sysDemo.getName());
        }

        //query.orderByAsc("id");

        return list(query);
    }
	
}
