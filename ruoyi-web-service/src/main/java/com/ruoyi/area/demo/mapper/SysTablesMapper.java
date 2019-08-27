package com.ruoyi.area.demo.mapper;

import com.ruoyi.area.demo.domain.SysTables;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 便签列 数据层
 *
 * @author jiyunsoft
 * @date 2019-08-21
 */

@Mapper
public interface SysTablesMapper extends BaseMapper<SysTables>
{

    public int updateTables(SysTables table);

   public  int  insertPeopleName(SysTables sysTables);

    public SysTables selectTableById(String tableId);

    public int updateTableInfo(SysTables obj);

    public List<SysTables> selectTableList(SysTables sysTables);

    public Integer checkTableNameUnique(String tableName);
}