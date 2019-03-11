package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysCalendarMapper;
import com.ruoyi.system.domain.SysCalendar;
import com.ruoyi.system.service.ISysCalendarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * 日历 服务层实现
 * 
 * @author jiyunsoft
 * @date 2019-03-09
 */
@Service
public class SysCalendarServiceImpl extends ServiceImpl<SysCalendarMapper, SysCalendar> implements ISysCalendarService 
{
    @Override
    public List<SysCalendar> selectList(SysCalendar sysCalendar) {
        QueryWrapper<SysCalendar> query = new QueryWrapper<>();
        // 查询条件

        return list(query);
    }
}
