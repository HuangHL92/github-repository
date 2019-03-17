package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 日历表 sys_calendar
 * 
 * @author jiyunsoft
 * @date 2019-03-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_calendar")
public class SysCalendar extends BaseEntity
{
    private static final long serialVersionUID = 1L;

	/** 数据标识 */
    @TableId
	private String id;
	/** 年度 */
	private Integer years;
	/** 日期 */
	private Integer days;
	/** 日期类型 */
	private Integer dayType;
	@TableField(exist = false)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date dateStr;
}
