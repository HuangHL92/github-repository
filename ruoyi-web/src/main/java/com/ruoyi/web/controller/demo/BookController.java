package com.ruoyi.web.controller.demo;

import java.util.Arrays;
import java.util.List;

import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysBook;
import com.ruoyi.system.service.ISysBookService;
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
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.framework.web.base.BaseController;
import com.ruoyi.common.page.TableDataInfo;
import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.support.Convert;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户 信息操作处理
 *
 * @author jiyunsoft
 * @date 2019-08-19
 */
@Controller
@RequestMapping("/demo/book")
public class BookController extends BaseController
{
    private String prefix = "/demo/book";

    @Autowired
    private ISysBookService sysBookService;

    @RequiresPermissions("system:sysBook:view")
    @GetMapping()
    public String sysBook()
    {
        return prefix + "/list";
    }

    /**
     * 查询用户列表
     */
    @RequiresPermissions("system:sysBook:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysBook sysBook)
    {
        startPage();
        return getDataTable(sysBookService.selectList(sysBook));
    }


    /**
     * 导出用户列表
     */
    @RequiresPermissions("system:sysBook:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysBook sysBook)
    {
        List<SysBook> list = sysBookService.selectList(sysBook);
        ExcelUtil<SysBook> util = new ExcelUtil<SysBook>(SysBook.class);
        return util.exportExcel(list, "sysBook");
    }

    /**
     * 新增用户
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        SysBook sysBook =new SysBook();
        //表单Action指定
        sysBook.setFormAction(prefix + "/add");
        mmap.put("sysBook", sysBook);
        return prefix + "/add";
    }

    /**
     * 新增保存用户
     */
    @RequiresPermissions("system:sysBook:add")
    @Log(title = "用户", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysBook sysBook,HttpServletRequest request, Model model)
    {
        return toAjax(sysBookService.save(sysBook));
    }

    /**
     * 修改用户
     */
    @GetMapping("/edit/{bookId}")
    public String edit(@PathVariable("bookId") String bookId, ModelMap mmap)
    {
        SysBook sysBook = sysBookService.getById(bookId);
        //表单Action指定
        sysBook.setFormAction(prefix + "/edit");
        //主键加密（配合editSave方法使用）- 如果需防止数据ID泄露，请放开，否则请删除此处代码
        //sysUser.setId(pk_encrypt(sysUser.getId()));

        mmap.put("sysBook", sysBook);
        return prefix + "/add";
    }

    /**
     * 修改保存用户
     */
    @RequiresPermissions("system:sysBook:edit")
    @Log(title = "用户", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysBook sysBook)
    {

        //主键解密（配合edit方法使用，请确认edit方法中加密了）- 如果需防止数据ID泄露，请放开，否则请删除此处代码
        //sysUser.setId(pk_decrypt(sysUser.getId()));

        return toAjax(sysBookService.saveOrUpdate(sysBook));
    }

    /**
     * 删除用户
     */
    @RequiresPermissions("system:sysBook:remove")
    @Log(title = "用户", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysBookService.removeByIds(Arrays.asList(Convert.toStrArray(ids))));
    }

}
