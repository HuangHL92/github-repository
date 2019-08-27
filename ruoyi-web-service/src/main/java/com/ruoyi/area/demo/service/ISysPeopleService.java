package com.ruoyi.area.demo.service;

import com.ruoyi.area.demo.domain.SysPeople;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.area.demo.domain.SysTables;
import com.ruoyi.area.demo.mapper.SysPeopleMapper;

import java.util.List;

/**
 * 人员 服务层
 *
 * @author jiyunsoft
 * @date 2019-08-21
 */
public interface ISysPeopleService extends IService<SysPeople>
{
    List<SysPeople> selectList(SysPeople sysPeople);

     List<SysPeople> selectPostAll();


  public  List<SysPeople> selectPeoplesByTableId(String tableId);
}

