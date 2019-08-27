package com.ruoyi.area.demo.domain;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 便签列表 sys_tables
 *
 * @author jiyunsoft
 * @date 2019-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_tables")
public class SysTables extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 便签id */
    @TableId
    private String tableId;
    /** 标签类型 */
    private String tableType;
    /** 标签名字 */
    private String tableName;
    /** 状态（1为正常，0不正常） */
    private String  tableStatus;
    /** 人员 */
    @TableField(exist = false)
    private String tablePeoples;
    /** 说明 */
    private String tableSys;
//排序
    private Long tableInfo;
   @TableField(exist = false)
    private String  pepleName;
    @TableField(exist = false)
    private Map<String, Object> parans;




}
