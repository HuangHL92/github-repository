package com.ruoyi.area.demo.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.util.Date;

/**
 * div表 sys_cache
 *
 * @author jiyunsoft
 * @date 2019-08-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_cache")
public class SysCache extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户id */
    @TableId
    private String userId;
    /** 用户数量 */
    private String userNuber;
    /** 用户姓名 */
    private String userName;
    /** 创建时间 */
    private Date createDate;
    /** 更新时间 */
    private Date upDate;
}
