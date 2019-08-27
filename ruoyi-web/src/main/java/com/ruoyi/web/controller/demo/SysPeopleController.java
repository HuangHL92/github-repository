package com.ruoyi.web.controller.demo;
import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.area.demo.domain.SysPeople;
import com.ruoyi.area.demo.service.ISysPeopleService;
import com.ruoyi.framework.web.base.BaseController;
import com.ruoyi.common.page.TableDataInfo;
import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.support.Convert;
import javax.servlet.http.HttpServletRequest;

/**
 * 人员 信息操作处理
 *
 * @author jiyunsoft
 * @date 2019-08-21
 */
@Controller
@RequestMapping("/demo/sysPeople")
public class SysPeopleController extends BaseController
{
    private String prefix = "demo/sysPeople";

    @Autowired
    private ISysPeopleService sysPeopleService;

    @RequiresPermissions("demo:sysPeople:view")
    @GetMapping()
    public String sysPeople()
    {
        return prefix + "/list";
    }

    /**
     * 查询人员列表
     */
    @RequiresPermissions("demo:sysPeople:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysPeople sysPeople)
    {
        startPage();
        return getDataTable(sysPeopleService.selectList(sysPeople));
    }


    /**
     * 导出人员列表
     */
    @RequiresPermissions("demo:sysPeople:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysPeople sysPeople)
    {
        List<SysPeople> list = sysPeopleService.selectList(sysPeople);
        ExcelUtil<SysPeople> util = new ExcelUtil<SysPeople>(SysPeople.class);
        return util.exportExcel(list, "sysPeople");
    }

    /**
     * 新增人员
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        SysPeople sysPeople  = new SysPeople();
        //表单Action指定
        sysPeople.setFormAction(prefix + "/add");
        mmap.put("sysPeople", sysPeople);
        return prefix + "/add";
    }

    /**
     * 新增保存人员
     */
    @RequiresPermissions("demo:sysPeople:add")
    @Log(title = "人员", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysPeople sysPeople,HttpServletRequest request, Model model)
    {
        return toAjax(sysPeopleService.save(sysPeople));
    }

    /**
     * 修改人员
     */
    @GetMapping("/edit/{peopleId}")
    public String edit(@PathVariable("peopleId") Integer peopleId, ModelMap mmap)
    {
        SysPeople sysPeople = sysPeopleService.getById(peopleId);
        //表单Action指定
        sysPeople.setFormAction(prefix + "/edit");
        //主键加密（配合editSave方法使用）- 如果需防止数据ID泄露，请放开，否则请删除此处代码
        //sysPeople.setId(pk_encrypt(sysPeople.getId()));

        mmap.put("sysPeople", sysPeople);
        return prefix + "/add";
    }

    /**
     * 修改保存人员
     */
    @RequiresPermissions("demo:sysPeople:edit")
    @Log(title = "人员", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysPeople sysPeople)
    {

        //主键解密（配合edit方法使用，请确认edit方法中加密了）- 如果需防止数据ID泄露，请放开，否则请删除此处代码
        //sysPeople.setId(pk_decrypt(sysPeople.getId()));

        return toAjax(sysPeopleService.saveOrUpdate(sysPeople));
    }

    /**
     * 删除人员
     */
    @RequiresPermissions("demo:sysPeople:remove")
    @Log(title = "人员", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysPeopleService.removeByIds(Arrays.asList(Convert.toStrArray(ids))));
    }

}