package com.ruoyi.area.demo.domain;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.util.Date;

/**
 * 人员表 sys_people
 *
 * @author jiyunsoft
 * @date 2019-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_people")
public class SysPeople extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 人员id */
    @TableId
    private Integer peopleId;
    /** 人员编码 */
    private String peopleCode;
    /** 人员名称 */
    private String pepleName;
    /** 显示顺序 */
    private Integer peopleSort;
    /** 状态（0正常，1停用） */
    private String status;
    @TableField(exist = false)
    private boolean flag = false;
}

