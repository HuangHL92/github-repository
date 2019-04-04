package com.ruoyi.tool.table.service;


import com.ruoyi.tool.table.domain.GenTable;

import java.util.List;

/**
 * @author: xjm
 * @date: 2019/04/01
 */
public interface IGenTableService {

    /**
     * 获取所有表
     * @param genTable
     * @return
     */
    public List<GenTable> selectAllList(GenTable genTable);

    /**
     * 获取指定id的table
     * @param genTableId
     * @return
     */
    public GenTable selectTableById(String genTableId);

    /**
     * 添加表
     * @param genTable
     * @return
     */
    public boolean insertTable(GenTable genTable);

    /**
     * 更新表
     * @param genTable
     * @return
     */
    public boolean updateTable(GenTable genTable);

    /**
     * 创建表的sql
     * @param sql
     */
    public int buildTable(String sql);

    /**
     * 移除表
     * @param ids
     */
    public void removeTable(String ids);

    /**
     * 删除表
     * @param ids
     */
    public void deleteTable(String ids);


    /**
     * 同步数据库
     * @param id
     * @param isForce
     */
    void synchDb(String id, boolean isForce);

    /**
     * 验证表是否存在
     * @param table
     * @return
     */
    public boolean checkIsTableExist(GenTable table);

}
