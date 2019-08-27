package com.ruoyi.web.controller.demo;

import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.ruoyi.area.demo.service.ISysPeopleService;
import com.ruoyi.area.demo.service.TableAndPeopleService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.util.CacheUtils;
import com.ruoyi.framework.util.JsonFileUtils;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.web.websocket.SocketServer;
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
import com.ruoyi.area.demo.domain.SysTables;
import com.ruoyi.area.demo.service.ISysTablesService;
import com.ruoyi.framework.web.base.BaseController;
import com.ruoyi.common.page.TableDataInfo;
import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.support.Convert;

import javax.servlet.http.HttpServletRequest;

/**
 * 便签列 信息操作处理
 *
 * @author jiyunsoft
 * @date 2019-08-21
 */
@Controller
@RequestMapping("/demo/sysTables")
public class SysTablesController extends BaseController {
    private String prefix = "demo/sysTables";
    @Autowired
    ISysPeopleService ISysPeopleService;
    @Autowired
    private ISysTablesService sysTablesService;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    TableAndPeopleService tableAndPeopleService;
    private ISysPeopleService peopleService;
    @RequiresPermissions("demo:sysTables:view")
    @GetMapping()
    public String sysTables() {
        return prefix + "/list";
    }

    /**
     * 查询便签列列表
     */
    @RequiresPermissions("demo:sysTables:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysTables sysTables) {

        startPage();
        PageHelper.clearPage();
        return getDataTable(sysTablesService.selectList(sysTables));
    }


    /**
     * 导出便签列列表
     */
    @RequiresPermissions("demo:sysTables:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysTables sysTables) {
        List<SysTables> list = sysTablesService.selectList(sysTables);
        ExcelUtil<SysTables> util = new ExcelUtil<SysTables>(SysTables.class);
        return util.exportExcel(list, "sysTables");
    }

    /**
     * 新增便签列
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        SysTables sysTables = new SysTables();
        //表单Action指定
        sysTables.setFormAction(prefix + "/add");
        mmap.put("sysTables", sysTables);
        mmap.put("posts", ISysPeopleService.selectPostAll());
        return prefix + "/add";
    }

    /**
     * 新增保存便签列
     */
    @RequiresPermissions("demo:sysTables:add")
    @Log(title = "便签列", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysTables sysTables, HttpServletRequest request, Model model,String name) {
//        sysTablesService.addPeople(sysTables);
       boolean b= sysTablesService.save(sysTables);

            String tId=sysTables.getTableId();

        String[] pIds = name.split(",");

        tableAndPeopleService.insertTableAndPeople(pIds,tId);

        return toAjax(b);


    }

    /**
     * 修改便签列
     */
    @GetMapping("/edit/{tableId}")
    public String edit(@PathVariable("tableId") String tableId, ModelMap mmap) {
        SysTables sysTables = sysTablesService.getById(tableId);
        //表单Action指定
//        sysTables.setFormAction(prefix + "/edit");
        //主键加密（配合editSave方法使用）- 如果需防止数据ID泄露，请放开，否则请删除此处代码
        //sysTables.setId(pk_encrypt(sysTables.getId()));

        mmap.put("sysTables", sysTables);
        mmap.put("posts", ISysPeopleService.selectPeoplesByTableId(tableId));
        return prefix + "/edit";
    }

    /**
     * 修改保存便签列
     */
    @RequiresPermissions("demo:sysTables:edit")
    @Log(title = "便签列", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysTables sysTables, String post) {

        //主键解密（配合edit方法使用，请确认edit方法中加密了）- 如果需防止数据ID泄露，请放开，否则请删除此处代码
        //sysTables.setId(pk_decrypt(sysTables.getId()));

//        return toAjax(sysTablesService.saveOrUpdate(sysTables));
        String tId=sysTables.getTableId();

        String[] pIds = post.split(",");

        tableAndPeopleService.updateTableAndPeople(pIds,tId);
        boolean flag = sysTablesService.updateById(sysTables);
        sendRefreshMsg(flag);
        return toAjax(flag);
    }

    /**
     * 删除便签列
     */
    @RequiresPermissions("demo:sysTables:remove")
    @Log(title = "便签列", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(sysTablesService.removeByIds(Arrays.asList(Convert.toStrArray(ids))));
    }

    @PostMapping("/checkLoginNameUnique")
    @ResponseBody
    public String checkLoginNameUnique(SysTables sysTables) {

        return sysTablesService.checkLoginNameUnique(sysTables.getTableName());
    }
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(SysTables table) {
        // 清空用户缓存
//        cacheUtils.getUserCache().remove(table.getLoginName());
        // 删除组织结构json文件
//        JsonFileUtils.deleteOrgJsonFile();
        return toAjax(sysTablesService.changeStatus(table));
    }
    @GetMapping("/updateOrder")
    @ResponseBody
    public AjaxResult updateOrder(String tableId, String tableInfo) {
        try {
            SysTables obj = sysTablesService.selectTableById(tableId);
            if (obj != null) {
                obj.setTableInfo(Long.valueOf(tableInfo));
                sysTablesService.updateTableInfo(obj);
            }
        } catch (Exception ex) {
            return AjaxResult.error("更新失败！");
        }

        return AjaxResult.success();
    }
    private void sendRefreshMsg(boolean flag) {
        if (flag) {
            SocketServer.sendMessage("model2", "websocketDemo");
        }
    }
}

