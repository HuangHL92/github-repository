package com.ruoyi.system.domain;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 这是一个学生表 sys_student
 *
 * @author jiyunsoft
 * @date 2019-08-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_student")
public class SysStudent extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    @TableId
    private Integer studentId;
    /**  */
    private String studentName;
    /**  */
    private Integer studentAge;
    /**  */
    private String studentSex;
}

