package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.base.BaseEntity;

/**
 * 测试表 sys_demo
 * 
 * @author ruoyi
 * @date 2019-01-18
 */
public class Demo extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 数据标识 */
    @Excel(name = "编号")
	private String id;

	/** 姓名 */
    @Excel(name = "名称")
	private String name;

	public void setId(String id) 
	{
		this.id = id;
	}

	public String getId() 
	{
		return id;
	}
	public void setName(String name) 
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .toString();
    }
}
