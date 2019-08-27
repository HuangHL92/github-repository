package com.ruoyi.area.demo.service.impl;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.area.demo.domain.TableAndPeople;
import com.ruoyi.area.demo.mapper.TableAndPeopleMapper;
import com.ruoyi.area.demo.service.TableAndPeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TableAndPeopleServiceImpl extends ServiceImpl<TableAndPeopleMapper, TableAndPeople> implements TableAndPeopleService {
    @Autowired
    TableAndPeopleMapper tableAndPeopleMapper;

    @Override
    public int insertTableAndPeople(String[] pIds, String tId) {
        TableAndPeople a = new TableAndPeople();
        for (String pId : pIds) {
            a.setPId(pId);
            a.setTId(tId);
            tableAndPeopleMapper.insert(a);

        }
        return 0;
    }

    @Override
    public int updateTableAndPeople(String[] pIds, String tId) {
        UpdateWrapper<TableAndPeople> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("t_Id",tId);
        tableAndPeopleMapper.delete(updateWrapper);
        TableAndPeople a = new TableAndPeople();
        for (String pId : pIds) {
            a.setPId(pId);
            a.setTId(tId);
            tableAndPeopleMapper.insert(a);
        }



        return 0;
    }
}
