package com.ruoyi.area.demo.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.area.demo.domain.SysTables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.area.demo.mapper.SysPeopleMapper;
import com.ruoyi.area.demo.domain.SysPeople;
import com.ruoyi.area.demo.service.ISysPeopleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;

/**
 * 人员 服务层实现
 *
 * @author jiyunsoft
 * @date 2019-08-21
 */
@Service
public class SysPeopleServiceImpl extends ServiceImpl<SysPeopleMapper, SysPeople> implements ISysPeopleService
{
    @Autowired
    SysPeopleMapper sysPeopleMapper;
    @Override
    public List<SysPeople> selectList(SysPeople sysPeople) {
        QueryWrapper<SysPeople> query = new QueryWrapper<>();
        // 查询条件

        return list(query);
    }

    @Override
    public List<SysPeople> selectPostAll() {
        return sysPeopleMapper.selectPeopleAll();
    }

    @Override
    public List<SysPeople> selectPeoplesByTableId(String tableId) {
        List<SysPeople> sysPeoples = sysPeopleMapper.selectPeopleAll();
        List<SysPeople> sysPeopled =sysPeopleMapper.selectpeoplesByTableId(tableId);
        for(SysPeople a:sysPeoples){
            for(SysPeople b:sysPeopled){
                if(a.getPeopleId().longValue()==b.getPeopleId().longValue()){
                    a.setFlag(true);
                    break;

                }
            }
        }
        return sysPeoples;
    }


}
