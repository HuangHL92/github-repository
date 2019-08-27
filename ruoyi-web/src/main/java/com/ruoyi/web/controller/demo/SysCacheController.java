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
import com.ruoyi.area.demo.domain.SysCache;
import com.ruoyi.area.demo.service.SysCacheService;
import com.ruoyi.framework.web.base.BaseController;
import com.ruoyi.common.page.TableDataInfo;
import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.support.Convert;
import javax.servlet.http.HttpServletRequest;

/**
 * div 信息操作处理
 *
 * @author jiyunsoft
 * @date 2019-08-20
 */
@Controller
@RequestMapping("/demo/sysCache")
public class SysCacheController extends BaseController
{
    private String prefix = "demo/sysCache";

    @Autowired
    private SysCacheService sysCacheService;

    @RequiresPermissions("demo:sysCache:view")
    @GetMapping()
    public String sysCache()
    {
        return prefix + "/list";
    }

    /**
     * 查询div列表
     */
    @RequiresPermissions("demo:sysCache:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysCache sysCache)
    {
        startPage();
        return getDataTable(sysCacheService.selectList(sysCache));
    }


    /**
     * 导出div列表
     */
    @RequiresPermissions("demo:sysCache:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysCache sysCache)
    {
        List<SysCache> list = sysCacheService.selectList(sysCache);
        ExcelUtil<SysCache> util = new ExcelUtil<SysCache>(SysCache.class);
        return util.exportExcel(list, "sysCache");
    }

    /**
     * 新增div
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        SysCache sysCache  = new SysCache();
        //表单Action指定
        sysCache.setFormAction(prefix + "/add");
        mmap.put("sysCache", sysCache);
        return prefix + "/add";
    }

    /**
     * 新增保存div
     */
    @RequiresPermissions("demo:sysCache:add")
    @Log(title = "div", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysCache sysCache,HttpServletRequest request, Model model)
    {
        return toAjax(sysCacheService.save(sysCache));
    }

    /**
     * 修改div
     */
    @GetMapping("/edit/{userId}")
    public String edit(@PathVariable("userId") String userId, ModelMap mmap)
    {
        SysCache sysCache = sysCacheService.getById(userId);
        //表单Action指定
        sysCache.setFormAction(prefix + "/edit");
        //主键加密（配合editSave方法使用）- 如果需防止数据ID泄露，请放开，否则请删除此处代码
        //sysCache.setId(pk_encrypt(sysCache.getId()));

        mmap.put("sysCache", sysCache);
        return prefix + "/add";
    }

    /**
     * 修改保存div
     */
    @RequiresPermissions("demo:sysCache:edit")
    @Log(title = "div", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysCache sysCache)
    {

        //主键解密（配合edit方法使用，请确认edit方法中加密了）- 如果需防止数据ID泄露，请放开，否则请删除此处代码
        //sysCache.setId(pk_decrypt(sysCache.getId()));

        return toAjax(sysCacheService.saveOrUpdate(sysCache));
    }

    /**
     * 删除div
     */
    @RequiresPermissions("demo:sysCache:remove")
    @Log(title = "div", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysCacheService.removeByIds(Arrays.asList(Convert.toStrArray(ids))));
    }

}

