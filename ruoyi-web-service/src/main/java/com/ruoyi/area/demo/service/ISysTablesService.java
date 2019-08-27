package com.ruoyi.area.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.area.demo.domain.SysTables;

import java.util.List;

/**
 * 便签列 服务层
 *
 * @author jiyunsoft
 * @date 2019-08-21
 */
public interface ISysTablesService extends IService<SysTables>
{
    List<SysTables> selectList(SysTables sysTables);

    String checkLoginNameUnique(String tableName);

    public int changeStatus(SysTables table);


  public   int addPeople(SysTables sysTables);


    public SysTables selectTableById(String tableId);


     public int updateTableInfo(SysTables obj);
}
