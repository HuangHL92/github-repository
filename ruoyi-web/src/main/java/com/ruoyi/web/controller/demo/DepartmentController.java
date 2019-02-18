package com.ruoyi.web.controller.demo;

import java.util.Arrays;
import java.util.List;

import cn.hutool.db.nosql.redis.RedisDS;
import com.ruoyi.demo.domain.Department;
import com.ruoyi.demo.service.IDepartmentService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.framework.web.base.BaseController;
import com.ruoyi.common.page.TableDataInfo;
import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.support.Convert;
import redis.clients.jedis.Jedis;

/**
 * 练习_部门 信息操作处理
 * 
 * @author jiyunsoft
 * @date 2019-02-14
 */
@Controller
@RequestMapping("/demo/department")
public class DepartmentController extends BaseController
{
    private String prefix = "demo/department";
	
	@Autowired
	private IDepartmentService departmentService;
	
	@RequiresPermissions("demo:department:view")
	@GetMapping()
	public String department()
	{
		Jedis jedis = RedisDS.create().getJedis();
		// NX是不存在时才set， XX是存在时才set， EX是秒，PX是毫秒
		jedis.set("aaa", "你好啊", "NX", "EX", 10);
	    return prefix + "/department";
	}
	
	/**
	 * 查询练习_部门列表
	 */
	@RequiresPermissions("demo:department:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(Department department)
	{
		startPage();
		return getDataTable(departmentService.selectList(department));
	}


	/**
	 * 导出练习_部门列表
	 */
	@RequiresPermissions("demo:department:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Department department)
    {
    	List<Department> list = departmentService.selectList(department);
        ExcelUtil<Department> util = new ExcelUtil<Department>(Department.class);
        return util.exportExcel(list, "department");
    }

	/**
	 * 新增练习_部门
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存练习_部门
	 */
	@RequiresPermissions("demo:department:add")
	@Log(title = "练习_部门", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(Department department)
	{		
		return toAjax(departmentService.save(department));
	}

	/**
	 * 修改练习_部门
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") String id, ModelMap mmap)
	{
		Department department = departmentService.getById(id);
		mmap.put("department", department);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存练习_部门
	 */
	@RequiresPermissions("demo:department:edit")
	@Log(title = "练习_部门", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(Department department)
	{		
		return toAjax(departmentService.updateById(department));
	}
	
	/**
	 * 删除练习_部门
	 */
	@RequiresPermissions("demo:department:remove")
	@Log(title = "练习_部门", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(departmentService.removeByIds(Arrays.asList(Convert.toStrArray(ids))));
	}
	
}
