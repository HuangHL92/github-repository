package com.ruoyi.area.demo.service.impl;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.area.demo.domain.Department;
import com.ruoyi.common.constant.UserConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.area.demo.mapper.SysTablesMapper;
import com.ruoyi.area.demo.domain.SysTables;
import com.ruoyi.area.demo.service.ISysTablesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;

/**
 * 便签列 服务层实现
 *
 * @author jiyunsoft
 * @date 2019-08-21
 */
@Service
public class SysTablesServiceImpl extends ServiceImpl<SysTablesMapper, SysTables> implements ISysTablesService
{
    @Autowired
    SysTablesMapper sysTablesMapper;
    @Override
    public List<SysTables> selectList(SysTables sysTables) {
//        QueryWrapper<SysTables> query = new QueryWrapper<>();
//        // 查询条件
//        // 部门名称模糊查询
//        query.lambda().like(StrUtil.isNotBlank(sysTables.getTableName()), SysTables::getTableName, sysTables.getTableName());
//        // 部门级别
//        query.lambda().eq(StrUtil.isNotBlank(sysTables.getTableType()), SysTables::getTableType, sysTables.getTableType());
//        // 创办日期范围查询
//        query.lambda().eq(StrUtil.isNotBlank(sysTables.getTableStatus()), SysTables::getTableStatus, sysTables.getTableStatus());
//        // 关键字模糊查询（联系人/联系电话/联系地址）
//        String keyword = sysTables.getParams().isEmpty() ? null : sysTables.getParams().get("keyword").toString();
//        query.lambda().and(
//                StrUtil.isNotBlank(keyword),
//                i -> i.like(SysTables::getTableName, keyword)
//                        .or().like(SysTables::getTableSys, keyword)
//                        .or().like(SysTables::getTablePeoples, keyword));
//        return list(query);

        return sysTablesMapper.selectTableList(sysTables);
    }

    @Override
    public String checkLoginNameUnique(String tableName) {
        int count=sysTablesMapper.checkTableNameUnique(tableName);
        if (count>0)
        {
            return UserConstants.USER_NAME_NOT_UNIQUE;
        }
        return UserConstants.USER_NAME_UNIQUE;
    }

    @Override
    public int changeStatus(SysTables table) {
        return  sysTablesMapper.updateTables(table);
    }

    @Override
    public int addPeople(SysTables sysTables) {
        return sysTablesMapper.insertPeopleName(sysTables);
    }

    @Override
    public SysTables selectTableById(String tableId) {
        return sysTablesMapper.selectTableById(tableId);
    }

    @Override
    public int  updateTableInfo(SysTables obj) {
        return sysTablesMapper.updateTableInfo(obj);
    }

}

