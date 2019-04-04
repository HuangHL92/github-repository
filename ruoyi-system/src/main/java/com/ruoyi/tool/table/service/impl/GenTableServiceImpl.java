package com.ruoyi.tool.table.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.tool.table.domain.GenTable;
import com.ruoyi.tool.table.domain.GenTableColumn;
import com.ruoyi.tool.table.mapper.GenTableColumnMapper;
import com.ruoyi.tool.table.mapper.GenTableMapper;
import com.ruoyi.tool.table.service.IGenTableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

/**
 * @author: xjm
 * @date: 2019/04/01
 */
@Service
public class GenTableServiceImpl extends ServiceImpl<GenTableMapper, GenTable> implements IGenTableService {

    private static final Logger log = LoggerFactory.getLogger(GenTableServiceImpl.class);
    @Autowired
    private GenTableColumnMapper genTableColumnMapper;

    @Override
    public List<GenTable> selectAllList(GenTable genTable) {
        QueryWrapper<GenTable> query = new QueryWrapper<>();
        query.lambda().and(StrUtil.isNotBlank(genTable.getComments()),
                i -> i.like(GenTable::getComments, genTable.getComments()));
        query.lambda().and(StrUtil.isNotBlank(genTable.getName()),
                i -> i.like(GenTable::getName, genTable.getName()));
        return list(query);
    }

    @Override
    @Transactional(readOnly = true)
    public GenTable selectTableById(String genTableId) {
        QueryWrapper<GenTable> query = new QueryWrapper<>();
        QueryWrapper<GenTableColumn> columnQuery = new QueryWrapper<>();
        query.lambda().eq(GenTable::getId, genTableId);
        GenTable genTable = getOne(query);
        columnQuery.lambda().eq(GenTableColumn::getGenTableId, genTable.getId());
        columnQuery.lambda().orderByAsc(GenTableColumn::getOrderNum);
        genTable.setAllColumnList(genTableColumnMapper.selectList(columnQuery));
        columnQuery.lambda().eq(GenTableColumn::getDelFlag, GenTableColumn.DEL_FLAG_NORMAL);
        genTable.setColumnList(genTableColumnMapper.selectList(columnQuery));
        return genTable;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertTable(GenTable genTable) {
        checkIsTableExist(genTable);
        genTable.setIsSync(GenTable.UN_SYNCHED);
        boolean result = save(genTable);
        List<GenTableColumn> columnList = genTable.getColumnList();
        for (GenTableColumn column : columnList) {
            column.setGenTableId(genTable.getId());
            column.setDelFlag(GenTableColumn.DEL_FLAG_NORMAL);
            genTableColumnMapper.insert(column);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTable(GenTable genTable) {
        UpdateWrapper<GenTable> updater = new UpdateWrapper<>();
        List<GenTableColumn> columnList = genTable.getColumnList();

        // 原记录
        GenTable old = this.selectTableById(genTable.getId());
        List<GenTableColumn> oldColumns = old.getAllColumnList(); // 全部column

        // 验证表单是否存在
        checkIsTableExist(genTable);

        // 验证是否有字段变更
        if (checkNeedSynch(genTable,old)) {
            updater.lambda().set(GenTable::getIsSync, GenTable.UN_SYNCHED); // 有字段变更把同步flag设为未同步
        }

        for (GenTableColumn oldColumn : oldColumns) {
            boolean isMatched = false;
            for (GenTableColumn newColumn : columnList) {
                if (oldColumn.getId().equals(newColumn.getId())) {
                    isMatched = true;
                    oldColumn.setOrderNum(newColumn.getOrderNum()); // 匹配到就更新排序
                    oldColumn.setJdbcType(newColumn.getJdbcType());
                    oldColumn.setName(newColumn.getName());
                    oldColumn.setComments(newColumn.getComments());
                    oldColumn.setIsPk(newColumn.getIsPk());
                    // 必须要之前同步过的table才能设置old*
                    if (!oldColumn.getName().equals(newColumn.getName())
                            && GenTable.SYNCHED.equals(genTable.getIsSync())) {
                        oldColumn.setOldName(oldColumn.getName()); // 先记录old，然后再赋值new
                    }
                    if (!oldColumn.getComments().equals(newColumn.getComments())
                            && GenTable.SYNCHED.equals(genTable.getIsSync())) {
                        oldColumn.setOldComments(oldColumn.getComments());
                    }
                    if (!oldColumn.getIsPk().equals(newColumn.getIsPk())
                            && GenTable.SYNCHED.equals(genTable.getIsSync())) {
                        oldColumn.setOldIsPk(oldColumn.getIsPk());
                    }
                    if (!oldColumn.getJdbcType().equals(newColumn.getJdbcType())
                            && GenTable.SYNCHED.equals(genTable.getIsSync())) {
                        oldColumn.setOldJdbcType(oldColumn.getJdbcType());
                    }
                }
            }
            if (!isMatched) {
                oldColumn.setDelFlag(GenTableColumn.DEL_FLAG_DELETE); // 没匹配到删除
            }
            genTableColumnMapper.updateById(oldColumn);
        }

        for (GenTableColumn newColumn : columnList) {
            boolean isMatched = false;
            for (GenTableColumn oldColumn : oldColumns) {
                if (oldColumn.getId().equals(newColumn.getId())) {
                    isMatched = true;
                }
            }
            if (!isMatched) {
                newColumn.setGenTableId(old.getId());
                genTableColumnMapper.insert(newColumn); // 没匹配到新增
            }
        }

        // 更新gen_table字段
        updater.lambda().set(GenTable::getName, genTable.getName());
        updater.lambda().set(GenTable::getComments, genTable.getComments());
        updater.lambda().set(GenTable::getGenIdType, genTable.getGenIdType());
        // 必须要之前同步过的table才能设置old*
        if (!old.getName().equals(genTable.getName())
                && GenTable.SYNCHED.equals(genTable.getIsSync())) {
            updater.lambda().set(GenTable::getOldName, old.getName());
        }
        if (!old.getComments().equals(genTable.getComments())
                && GenTable.SYNCHED.equals(genTable.getIsSync())) {
            updater.lambda().set(GenTable::getOldComments, old.getComments());
        }
        if (!old.getGenIdType().equals(genTable.getGenIdType())
                && GenTable.SYNCHED.equals(genTable.getIsSync())) {
            updater.lambda().set(GenTable::getOldGenIdType, old.getGenIdType());
        }
        updater.lambda().eq(GenTable::getId, old.getId());

        return update(updater);
    }


    @Override
    public int buildTable(String sql) {
        GenTableMapper genTableMapper = getBaseMapper();
        return genTableMapper.buildTable(sql);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeTable(String ids) {
        List<String> idList = CollUtil.newArrayList(ids.split(","));
        // 删除对应的column表
        idList.forEach(id -> {
            UpdateWrapper<GenTableColumn> updater = new UpdateWrapper<>();
            updater.lambda().eq(GenTableColumn::getGenTableId, id);
            genTableColumnMapper.delete(updater);
        });
        removeByIds(idList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTable(String ids) {
        List<String> idList = CollUtil.newArrayList(ids.split(","));
        // 删除对应的column表
        idList.forEach(id -> {
            GenTable genTable = this.getBaseMapper().selectById(id);
            StringBuffer sb = new StringBuffer();
            sb.append("drop table if exists " + genTable.getName() + " ;");
            this.buildTable(sb.toString()); // 物理删除表
            UpdateWrapper<GenTableColumn> updater = new UpdateWrapper<>();
            updater.lambda().eq(GenTableColumn::getGenTableId, id);
            genTableColumnMapper.delete(updater); // 删除gen_table_column表数据
        });
        removeByIds(idList);
    }

    /**
     * 同步数据库（DDL）
     * @param id
     * @param isForce
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void synchDb(String id, boolean isForce) {

        GenTable orgTable = this.selectTableById(id);
        List<GenTableColumn> columnList = orgTable.getColumnList();
        List<GenTableColumn> allColumnList = orgTable.getAllColumnList();
        UpdateWrapper<GenTable> tableUpdater;
        UpdateWrapper<GenTableColumn> columnUpdater;
        StringBuffer sb;
        String columnName;
        String oldColumnName;
        String newColumnName;
        String jdbcType;
        String pk;
        Iterator iterator;
        GenTableColumn genTableColumn;

        try {

            // 普通同步并且不是新表
            if (!isForce && orgTable.getOldName() != null) {
                sb = new StringBuffer();
                if (!orgTable.getName().equalsIgnoreCase(orgTable.getOldName())) {
                    sb.append("ALTER  TABLE " + orgTable.getOldName() + " RENAME TO " + orgTable.getName() + ";");
                    this.buildTable("ALTER  TABLE " + orgTable.getOldName() + " RENAME TO " + orgTable.getName() + ";");
                    tableUpdater = new UpdateWrapper<>();
                    tableUpdater.lambda().set(GenTable::getOldName, orgTable.getName());
                    tableUpdater.lambda().eq(GenTable::getId, orgTable.getId());
                    update(tableUpdater);
                }
                if (!orgTable.getComments().equals(orgTable.getOldComments())) {
                    sb.append("alter table " + orgTable.getName() + " comment '" + orgTable.getComments() + "';");
                    this.buildTable("alter table " + orgTable.getName() + " comment '" + orgTable.getComments() + "';");
                    tableUpdater = new UpdateWrapper<>();
                    tableUpdater.lambda().set(GenTable::getOldComments, orgTable.getComments());
                    update(tableUpdater);
                }

                // 判断是否有字段需要删除
                iterator = allColumnList.iterator();
                while (iterator.hasNext()) {
                    genTableColumn = (GenTableColumn) iterator.next();
                    oldColumnName = genTableColumn.getOldName();
                    if (genTableColumn.getDelFlag().equals(GenTableColumn.DEL_FLAG_DELETE)
                            && this.isColumnExistInTable(orgTable.getName(), oldColumnName)) {
                        sb.append("alter table " + orgTable.getName() + " drop " + oldColumnName + ";");
                        this.buildTable("alter table " + orgTable.getName() + " drop " + oldColumnName + ";");
                        columnUpdater = new UpdateWrapper<>();
                        columnUpdater.lambda().eq(GenTableColumn::getId, genTableColumn.getId());
                        genTableColumnMapper.delete(columnUpdater); // 同步完成后把标记为已删除的字段物理删除
                    }
                }

                iterator = allColumnList.iterator();

                mark:
                while (true) {
                    do {
                        do {
                            do {
                                if (!iterator.hasNext()) { // 是否是最后一条
                                    iterator = allColumnList.iterator();

                                    while (iterator.hasNext()) {
                                        genTableColumn = (GenTableColumn) iterator.next();
                                        oldColumnName = genTableColumn.getOldName();
                                        newColumnName = genTableColumn.getName();
                                        if (!genTableColumn.getDelFlag().equals(GenTableColumn.DEL_FLAG_DELETE) && oldColumnName == null) {
                                            sb.append("alter table " + orgTable.getName() + " add " + newColumnName + " " + genTableColumn.getJdbcType() + " comment '" + genTableColumn.getComments() + "';");
                                            this.buildTable("alter table " + orgTable.getName() + " add " + newColumnName + " " + genTableColumn.getJdbcType() + " comment '" + genTableColumn.getComments() + "';");
                                            columnUpdater = new UpdateWrapper<>();
                                            columnUpdater.lambda().eq(GenTableColumn::getId, genTableColumn.getId());
                                            columnUpdater.lambda().set(GenTableColumn::getOldName, genTableColumn.getName());
                                            columnUpdater.lambda().set(GenTableColumn::getOldComments, genTableColumn.getComments());
                                            columnUpdater.lambda().set(GenTableColumn::getOldIsPk, genTableColumn.getIsPk());
                                            columnUpdater.lambda().set(GenTableColumn::getOldJdbcType, genTableColumn.getJdbcType());
                                            genTableColumnMapper.update(null, columnUpdater);
                                        }
                                    }

                                    if (orgTable.getGenIdType() != null && !orgTable.getGenIdType().equals(orgTable.getOldGenIdType())) {
                                        iterator = allColumnList.iterator();
                                        while (iterator.hasNext()) {
                                            genTableColumn = (GenTableColumn) iterator.next();
                                            if (!genTableColumn.getDelFlag().equals(GenTableColumn.DEL_FLAG_DELETE) && genTableColumn.getName() != null && genTableColumn.getIsPk().equals(GenTableColumn.YES)) {
                                                if (orgTable.getGenIdType().equals(GenTable.IDTYPE_AUTO)) {
                                                    jdbcType = genTableColumn.getJdbcType();
                                                    if (!jdbcType.toLowerCase().contains("int") && !jdbcType.toLowerCase().contains("integer")) {
                                                        jdbcType = "integer";
                                                        columnUpdater = new UpdateWrapper<>();
                                                        columnUpdater.lambda().set(GenTableColumn::getJdbcType, jdbcType);
                                                        columnUpdater.lambda().set(GenTableColumn::getOldJdbcType, jdbcType);
                                                        columnUpdater.lambda().eq(GenTableColumn::getId, genTableColumn.getId());
                                                        genTableColumnMapper.update(null, columnUpdater);
                                                    }
                                                    this.buildTable("alter table " + orgTable.getName() + " change   " + genTableColumn.getName() + " " + genTableColumn.getName() + " " + jdbcType + " auto_increment ;");
                                                }
                                            } else {
                                                jdbcType = genTableColumn.getJdbcType();
                                                if (!jdbcType.toLowerCase().contains("varchar")) {
                                                    jdbcType = "varchar(64)";
                                                    genTableColumn.setJdbcType(jdbcType);
                                                    columnUpdater = new UpdateWrapper<>();
                                                    columnUpdater.lambda().set(GenTableColumn::getJdbcType, jdbcType);
                                                    columnUpdater.lambda().set(GenTableColumn::getOldJdbcType, jdbcType);
                                                    columnUpdater.lambda().eq(GenTableColumn::getId, genTableColumn.getId());
                                                    genTableColumnMapper.update(null, columnUpdater);
                                                }
                                                this.buildTable("alter table " + orgTable.getName() + " change   " + genTableColumn.getName() + " " + genTableColumn.getName() + " " + jdbcType + " ;");
                                            }
                                        }
                                    }

                                    jdbcType = "";
                                    pk = "";
                                    iterator = columnList.iterator();

                                    while (iterator.hasNext()) {
                                        genTableColumn = (GenTableColumn) iterator.next();
                                        if (genTableColumn.getIsPk().equals(GenTableColumn.YES)) {
                                            jdbcType = jdbcType + genTableColumn.getName() + ",";
                                        }

                                        if (GenTableColumn.YES.equals(genTableColumn.getOldIsPk())) {
                                            pk = pk + genTableColumn.getName() + ",";
                                        }
                                    }

                                    if (!pk.equals(jdbcType)) {
                                        sb.append("alter table " + orgTable.getName() + " drop primary key;");
                                        this.buildTable("alter table " + orgTable.getName() + " drop primary key;");
                                        iterator = columnList.iterator();

                                        while (iterator.hasNext()) {
                                            genTableColumn = (GenTableColumn) iterator.next();
                                            if (GenTableColumn.YES.equals(genTableColumn.getOldIsPk())) {
                                                columnUpdater = new UpdateWrapper<>();
                                                columnUpdater.lambda().set(GenTableColumn::getOldIsPk, GenTableColumn.NO);
                                                columnUpdater.lambda().eq(GenTableColumn::getId, genTableColumn.getId());
                                                genTableColumnMapper.update(null, columnUpdater);
                                            }
                                        }

                                        if (jdbcType.length() > 0) {
                                            sb.append("alter table " + orgTable.getName() + " add  CONSTRAINT PK_SJ_RESOURCE_CHARGES PRIMARY KEY(" + jdbcType.substring(0, jdbcType.length() - 1) + ");");
                                            this.buildTable("alter table " + orgTable.getName() + " add  CONSTRAINT PK_SJ_RESOURCE_CHARGES PRIMARY KEY(" + jdbcType.substring(0, jdbcType.length() - 1) + ");");
                                            iterator = columnList.iterator();

                                            while (iterator.hasNext()) {
                                                genTableColumn = (GenTableColumn) iterator.next();
                                                if (GenTableColumn.YES.equals(genTableColumn.getIsPk())) {
                                                    columnUpdater = new UpdateWrapper<>();
                                                    columnUpdater.lambda().set(GenTableColumn::getOldIsPk, GenTableColumn.YES);
                                                    columnUpdater.lambda().eq(GenTableColumn::getId, genTableColumn.getId());
                                                    genTableColumnMapper.update(null, columnUpdater);
                                                }
                                            }
                                        }
                                    }

                                    break mark;
                                }
                                genTableColumn = (GenTableColumn) iterator.next();
                                oldColumnName = genTableColumn.getOldName();
                                newColumnName = genTableColumn.getName();
                            } while (genTableColumn.getDelFlag().equals(GenTableColumn.DEL_FLAG_DELETE));
                        } while (oldColumnName == null);
                    } while (newColumnName.equals(oldColumnName) && genTableColumn.getJdbcType().equals(genTableColumn.getOldJdbcType())
                            && genTableColumn.getComments().equals(genTableColumn.getOldComments()));

                    columnName = StringUtils.isBlank(genTableColumn.getOldName()) ? genTableColumn.getName() : genTableColumn.getOldName();
                    sb.append("alter table " + orgTable.getName() + " change  " + oldColumnName + " " + newColumnName + " " + genTableColumn.getJdbcType() + " comment '" + genTableColumn.getComments() + "';");
                    this.buildTable("alter table " + orgTable.getName() + " change  " + columnName + " " + genTableColumn.getName() + " " + genTableColumn.getJdbcType() + " comment '" + genTableColumn.getComments() + "';");
                    columnUpdater = new UpdateWrapper<>();
                    columnUpdater.lambda().set(GenTableColumn::getOldName, genTableColumn.getName());
                    columnUpdater.lambda().set(GenTableColumn::getOldComments, genTableColumn.getComments());
                    columnUpdater.lambda().set(GenTableColumn::getOldJdbcType, genTableColumn.getJdbcType());
                    columnUpdater.lambda().set(GenTableColumn::getOldIsPk, genTableColumn.getIsPk());
                    columnUpdater.lambda().eq(GenTableColumn::getId, genTableColumn.getId());
                    genTableColumnMapper.update(null, columnUpdater);
                }

            }
            // 强制同步或是没被创建过的新表（没有oldName意味着新表）
            else {
                sb = new StringBuffer();
                if (StringUtils.isNotBlank(orgTable.getOldName())) {
                    sb.append("drop table if exists " + orgTable.getOldName() + " ;");
                } else {
                    sb.append("drop table if exists " + orgTable.getName() + " ;");
                }

                this.buildTable(sb.toString());
                tableUpdater = new UpdateWrapper<>();
                tableUpdater.lambda().set(GenTable::getOldName, orgTable.getName());
                tableUpdater.lambda().eq(GenTable::getId, orgTable.getId());
                update(tableUpdater);
                sb = new StringBuffer();
                sb.append("create table " + orgTable.getName() + " (");
                pk = "";
                iterator = allColumnList.iterator();

                while (iterator.hasNext()) {
                    genTableColumn = (GenTableColumn) iterator.next();
                    if (!genTableColumn.getDelFlag().equals(GenTableColumn.DEL_FLAG_DELETE) && genTableColumn.getName() != null) {
                        if (genTableColumn.getIsPk().equals(GenTableColumn.YES)) {
                            if (orgTable.getGenIdType().equals(GenTable.IDTYPE_AUTO)) {
                                jdbcType = genTableColumn.getJdbcType();
                                if (!jdbcType.toLowerCase().contains("int") && !jdbcType.toLowerCase().contains("integer")) {
                                    jdbcType = "integer";
                                    genTableColumn.setJdbcType(jdbcType);
                                }

                                sb.append("  " + genTableColumn.getName() + " " + genTableColumn + " auto_increment  comment '" + genTableColumn.getComments() + "',");
                            } else {
                                jdbcType = genTableColumn.getJdbcType();
                                if (!jdbcType.toLowerCase().contains("varchar")) {
                                    jdbcType = "varchar(64)";
                                    genTableColumn.setJdbcType(jdbcType);
                                }

                                sb.append("  " + genTableColumn.getName() + " " + jdbcType + " comment '" + genTableColumn.getComments() + "',");
                            }

                            pk = pk + genTableColumn.getName() + ",";
                        } else {
                            sb.append("  " + genTableColumn.getName() + " " + genTableColumn.getJdbcType() + " comment '" + genTableColumn.getComments() + "',");
                        }
                    }
                }

                if (StringUtils.isNotEmpty(pk)) {
                    sb.append("primary key (" + pk.substring(0, pk.length() - 1) + ") ");
                }
                sb.append(") comment '" + orgTable.getComments() + "' DEFAULT CHARSET=utf8");
                this.buildTable(sb.toString()); // 重新建表成功!
            }
            // DDL后更新gen_table和gen_table_column记录
            updateRecord(orgTable);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException("数据异常，请重新操作");
        }
    }

    /**
     * 同步完成后更新记录
     * @param genTable
     */
    private void updateRecord(GenTable genTable) {
        genTable.setIsSync(GenTable.SYNCHED);
        genTable.setOldName(genTable.getName());
        genTable.setOldComments(genTable.getComments());
        genTable.setOldGenIdType(genTable.getGenIdType());
        Iterator iterator = genTable.getAllColumnList().iterator();

        while (iterator.hasNext()) {
            GenTableColumn genTableColumn = (GenTableColumn) iterator.next();
            if (GenTableColumn.DEL_FLAG_DELETE.equals(genTableColumn.getDelFlag())) {
                genTableColumnMapper.deleteById(genTableColumn.getId()); // 同步完成后物理删除记录
            } else {
                genTableColumn.setOldComments(genTableColumn.getComments());
                genTableColumn.setOldIsPk(genTableColumn.getIsPk());
                genTableColumn.setOldJdbcType(genTableColumn.getJdbcType());
                genTableColumn.setOldName(genTableColumn.getName());
                genTableColumnMapper.updateById(genTableColumn);
            }
        }

        updateById(genTable);
    }


    /**
     * 判断字段名是否存在于指定表
     * @param tableName
     * @param columnName
     * @return
     */
    private boolean isColumnExistInTable(String tableName, String columnName) {
        List<GenTableColumn> columnList = genTableColumnMapper.selectTableColumnListByTableName(tableName);
        Iterator iterator = columnList.iterator();

        GenTableColumn genTableColumn;
        do {
            if (!iterator.hasNext()) {
                return false;
            }

            genTableColumn = (GenTableColumn) iterator.next();
        } while (columnName == null || !columnName.equals(genTableColumn.getName()));

        return true;
    }

    /**
     * 验证table是否已经存在
     * @param table
     */
    private void checkIsTableExist(GenTable table) {
        // gen_table中是否存在
        QueryWrapper<GenTable> query = new QueryWrapper<>();
        query.lambda().eq(GenTable::getName, table.getName());
        List<GenTable> list = list(query);
        if (list != null && list.size() > 0) {
            if (list.get(0).getId().equals(table.getId())) {
                return;
            }
            throw new BusinessException("表单已存在");
        }
        // 物理表单是否被创建
        List<GenTable> genTables = this.getBaseMapper().selectByTableName(table.getName());
        if (genTables != null && genTables.size() > 0) {
            throw new BusinessException("表单已存在");
        }
    }

    /**
     * 验证是否有字段变更
     */
    private boolean checkNeedSynch(GenTable newTable, GenTable oldTable) {
        if (!oldTable.getName().equals(newTable.getName())) {
            return true;
        }
        if (!oldTable.getComments().equals(newTable.getComments())) {
            return true;
        }
        if (!oldTable.getGenIdType().equals(newTable.getGenIdType())) {
            return true;
        }
        List<GenTableColumn> oldColumns = oldTable.getAllColumnList();
        List<GenTableColumn> newColumns = newTable.getColumnList(); // 页面传来的column放在columnList属性中
        for (GenTableColumn oldColumn : oldColumns) {
            boolean isMatched = false;
            for (GenTableColumn newColumn : newColumns) {
                if (oldColumn.getId().equals(newColumn.getId())) {
                    isMatched = true;
                    if (!oldColumn.getName().equals(newColumn.getName())) {
                        return true;
                    }
                    if (!oldColumn.getComments().equals(newColumn.getComments())) {
                        return true;
                    }
                    if (!oldColumn.getIsPk().equals(newColumn.getIsPk())) {
                        return true;
                    }
                    if (!oldColumn.getJdbcType().equals(newColumn.getJdbcType())) {
                        return true;
                    }
                }
            }
            if (!isMatched) {
                return true; // 记录被删除了
            }
        }

        for (GenTableColumn newColumn : newColumns) {
            boolean isMatched = false;
            for (GenTableColumn oldColumn : oldColumns) {
                if (oldColumn.getId().equals(newColumn.getId())) {
                    isMatched = true;
                }
            }
            if (!isMatched) {
                return true; // 记录新增
            }
        }
        return false;
    }


}
