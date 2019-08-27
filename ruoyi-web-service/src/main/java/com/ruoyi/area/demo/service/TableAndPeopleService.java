package com.ruoyi.area.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.ruoyi.area.demo.domain.TableAndPeople;

public interface TableAndPeopleService extends IService<TableAndPeople> {

   public  int  insertTableAndPeople(String[] pIds, String tId);

    public int  updateTableAndPeople(String[] pIds, String tId);
}
