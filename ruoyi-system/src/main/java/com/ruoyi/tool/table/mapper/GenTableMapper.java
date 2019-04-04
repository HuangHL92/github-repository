package com.ruoyi.tool.table.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.tool.table.domain.GenTable;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GenTableMapper extends BaseMapper<GenTable>
{

    /**
     * 表单生成sql
     * @param sql
     * @return
     */
    public int buildTable(@Param("sql") String sql);

    public List<GenTable> selectByTableName(@Param("name") String tableName);

}
