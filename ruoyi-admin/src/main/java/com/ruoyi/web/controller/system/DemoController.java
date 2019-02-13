package com.ruoyi.web.controller.system;

import java.util.List;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.page.PageDomain;
import com.sun.jna.platform.win32.Guid;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.Demo;
import com.ruoyi.system.service.IDemoService;
import com.ruoyi.framework.web.base.BaseController;
import com.ruoyi.common.page.TableDataInfo;
import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 测试 信息操作处理
 * 
 * @author ruoyi
 * @date 2019-01-18
 */
@Controller
@RequestMapping("/system/demo")
public class DemoController extends BaseController
{
    private String prefix = "system/demo";
	
	@Autowired
	private IDemoService demoService;
	
	@RequiresPermissions("system:demo:view")
	@GetMapping()
	public String demo()
	{
	    return prefix + "/demo";
	}
	
	/**
	 * 查询测试列表
	 */
	@RequiresPermissions("system:demo:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(Demo demo)
	{
		startPage();
        List<Demo> list = demoService.selectDemoList(demo);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出测试列表
	 */
	@RequiresPermissions("system:demo:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Demo demo)
    {

        PageHelper.startPage(1, 999999, "name");
    	List<Demo> list = demoService.selectDemoList(demo);
        ExcelUtil<Demo> util = new ExcelUtil<Demo>(Demo.class);
        return util.exportExcel(list, "demo");
    }
	
	/**
	 * 新增测试
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存测试
	 */
	@RequiresPermissions("system:demo:add")
	@Log(title = "测试", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(Demo demo, HttpServletRequest request, HttpServletResponse response, Model model)
	{
        demo.setId(Guid.GUID.newGuid().toGuidString());
		return toAjax(demoService.insertDemo(demo));
	}

	/**
	 * 修改测试
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") String id, ModelMap mmap)
	{
		Demo demo = demoService.selectDemoById(id);
		mmap.put("demo", demo);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存测试
	 */
	@RequiresPermissions("system:demo:edit")
	@Log(title = "测试", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(Demo demo)
	{		
		return toAjax(demoService.updateDemo(demo));
	}
	
	/**
	 * 删除测试
	 */
	@RequiresPermissions("system:demo:remove")
	@Log(title = "测试", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(demoService.deleteDemoByIds(ids));
	}
	
}
