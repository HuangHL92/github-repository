package com.ruoyi.system.service.impl;

import cn.hutool.core.util.StrUtil;
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
        // 关键字模糊查询（年度/日期）
        String keyword = sysCalendar.getParams().isEmpty() ? null : sysCalendar.getParams().get("keyword").toString();

        query.lambda().and(
                StrUtil.isNotBlank(keyword),
                i -> i.like(SysCalendar::getYears, keyword)
                        .or().like(SysCalendar::getDays, keyword)

        );
        return list(query);
    }
}
