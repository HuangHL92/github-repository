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
import com.ruoyi.system.domain.SysStudent;
import com.ruoyi.system.service.ISysStudentService;
import com.ruoyi.framework.web.base.BaseController;
import com.ruoyi.common.page.TableDataInfo;
import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.support.Convert;
import javax.servlet.http.HttpServletRequest;

/**
 * 这是一个学生 信息操作处理
 *
 * @author jiyunsoft
 * @date 2019-08-20
 */
@Controller
@RequestMapping("/system/sysStudent")
public class SysStudentController extends BaseController
{
    private String prefix = "system/sysStudent";

    @Autowired
    private ISysStudentService sysStudentService;

    @RequiresPermissions("system:sysStudent:view")
    @GetMapping()
    public String sysStudent()
    {
        return prefix + "/list";
    }

    /**
     * 查询这是一个学生列表
     */
    @RequiresPermissions("system:sysStudent:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysStudent sysStudent)
    {
        startPage();
        return getDataTable(sysStudentService.selectList(sysStudent));
    }


    /**
     * 导出这是一个学生列表
     */
    @RequiresPermissions("system:sysStudent:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysStudent sysStudent)
    {
        List<SysStudent> list = sysStudentService.selectList(sysStudent);
        ExcelUtil<SysStudent> util = new ExcelUtil<SysStudent>(SysStudent.class);
        return util.exportExcel(list, "sysStudent");
    }

    /**
     * 新增这是一个学生
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        SysStudent sysStudent  = new SysStudent();
        //表单Action指定
        sysStudent.setFormAction(prefix + "/add");
        mmap.put("sysStudent", sysStudent);
        return prefix + "/add";
    }

    /**
     * 新增保存这是一个学生
     */
    @RequiresPermissions("system:sysStudent:add")
    @Log(title = "这是一个学生", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysStudent sysStudent,HttpServletRequest request, Model model)
    {
        return toAjax(sysStudentService.save(sysStudent));
    }

    /**
     * 修改这是一个学生
     */
    @GetMapping("/edit/{studentId}")
    public String edit(@PathVariable("studentId") Integer studentId, ModelMap mmap)
    {
        SysStudent sysStudent = sysStudentService.getById(studentId);
        //表单Action指定
        sysStudent.setFormAction(prefix + "/edit");
        //主键加密（配合editSave方法使用）- 如果需防止数据ID泄露，请放开，否则请删除此处代码
        //sysStudent.setId(pk_encrypt(sysStudent.getId()));

        mmap.put("sysStudent", sysStudent);
        return prefix + "/add";
    }

    /**
     * 修改保存这是一个学生
     */
    @RequiresPermissions("system:sysStudent:edit")
    @Log(title = "这是一个学生", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysStudent sysStudent)
    {

        //主键解密（配合edit方法使用，请确认edit方法中加密了）- 如果需防止数据ID泄露，请放开，否则请删除此处代码
        //sysStudent.setId(pk_decrypt(sysStudent.getId()));

        return toAjax(sysStudentService.saveOrUpdate(sysStudent));
    }

    /**
     * 删除这是一个学生
     */
    @RequiresPermissions("system:sysStudent:remove")
    @Log(title = "这是一个学生", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysStudentService.removeByIds(Arrays.asList(Convert.toStrArray(ids))));
    }

}

