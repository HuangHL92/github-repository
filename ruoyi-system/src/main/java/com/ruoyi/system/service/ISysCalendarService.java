package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysCalendar;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 日历 服务层
 * 
 * @author jiyunsoft
 * @date 2019-03-09
 */
public interface ISysCalendarService extends IService<SysCalendar>
{
    List<SysCalendar> selectList(SysCalendar sysCalendar);
}
