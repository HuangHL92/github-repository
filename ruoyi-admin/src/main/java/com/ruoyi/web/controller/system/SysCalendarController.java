package com.ruoyi.web.controller.system;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysCalendar;
import com.ruoyi.system.service.ISysCalendarService;
import com.ruoyi.framework.web.base.BaseController;
import com.ruoyi.common.page.TableDataInfo;
import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.support.Convert;

import javax.servlet.http.HttpServletRequest;

/**
 * 日历 信息操作处理
 * 
 * @author jiyunsoft
 * @date 2019-03-09
 */
@Controller
@RequestMapping("/system/calendar")
public class SysCalendarController extends BaseController
{
    private String prefix = "system/calendar";
	
	@Autowired
	private ISysCalendarService sysCalendarService;
	
	@RequiresPermissions("system:calendar:view")
	@GetMapping()
	public String sysCalendar()
	{
	    return prefix + "/list";
	}
	
	/**
	 * 查询日历列表
	 */
	@RequiresPermissions("system:calendar:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(SysCalendar sysCalendar)
	{
		startPage();
		return getDataTable(sysCalendarService.selectList(sysCalendar));
	}


	/**
	 * 导出日历列表
	 */
	@RequiresPermissions("system:calendar:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysCalendar sysCalendar)
    {
    	List<SysCalendar> list = sysCalendarService.selectList(sysCalendar);
        ExcelUtil<SysCalendar> util = new ExcelUtil<SysCalendar>(SysCalendar.class);
        return util.exportExcel(list, "canlendar");
    }

	/**
	 * 新增日历
	 */
	@GetMapping("/add")
	public String add(ModelMap mmap)
	{
        SysCalendar sysCalendar  = new SysCalendar();
        sysCalendar.setFormAction(prefix + "/add");
		mmap.put("sysCalendar", sysCalendar);
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存日历
	 */
	@RequiresPermissions("system:calendar:add")
	@Log(title = "日历", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(@RequestParam(value = "years",required = true)Integer years,
							  @RequestParam(value = "days",required = true)Date days,
							  @RequestParam(value = "dayType",required = false)Integer dayType,
							  HttpServletRequest request, Model model)
	{
		SysCalendar sysCalendar = new SysCalendar();
		int ts = Integer.valueOf(DateUtil.format(days,"yyyyMMdd"));

		sysCalendar.setDays(ts);
		sysCalendar.setYears(years);
		sysCalendar.setDayType(dayType);

		return toAjax(sysCalendarService.save(sysCalendar));
	}

	/**
	 * 修改日历
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") String id, ModelMap mmap)
	{
		SysCalendar sysCalendar = sysCalendarService.getById(id);

        sysCalendar.setFormAction(prefix + "/edit");
		mmap.put("sysCalendar", sysCalendar);
	    return prefix + "/add";
	}
	
	/**
	 * 修改保存日历
	 */
	@RequiresPermissions("system:calendar:edit")
	@Log(title = "日历", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(SysCalendar sysCalendar)
	{		
		return toAjax(sysCalendarService.saveOrUpdate(sysCalendar));
	}
	
	/**
	 * 删除日历
	 */
	@RequiresPermissions("system:calendar:remove")
	@Log(title = "日历", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(sysCalendarService.removeByIds(Arrays.asList(Convert.toStrArray(ids))));
	}
	
}
