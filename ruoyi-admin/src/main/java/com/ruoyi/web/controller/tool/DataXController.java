package com.ruoyi.web.controller.tool;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.page.TableDataInfo;
import com.ruoyi.common.support.Convert;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.base.BaseController;
import com.ruoyi.system.common.DataXJsonCommon;
import com.ruoyi.system.domain.SysDataX;
import com.ruoyi.system.service.ISysDataXService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.Arrays;
import java.util.List;

/**
 * Datax配置 信息操作处理
 *
 * @author jiyunsoft
 * @date 2019-04-02
 */
@Controller
@RequestMapping("/tool/dataX")
public class DataXController extends BaseController {
    private String prefix = "tool/dataX";

    @Autowired
    private ISysDataXService sysDataXService;

    @RequiresPermissions("tool:dataX:view")
    @GetMapping()
    public String dataX() {
        return prefix + "/list";
    }

    /**
     * 查询Datax配置列表
     */
    @RequiresPermissions("tool:dataX:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysDataX sysDataX) {
        startPage();
        return getDataTable(sysDataXService.selectList(sysDataX));
    }


    /**
     * 导出Datax配置列表
     */
    @RequiresPermissions("tool:dataX:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysDataX sysDataX) {
        List<SysDataX> list = sysDataXService.selectList(sysDataX);
        ExcelUtil<SysDataX> util = new ExcelUtil<SysDataX>(SysDataX.class);
        return util.exportExcel(list, "sysDataX");
    }

    /**
     * 新增Datax配置
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        SysDataX sysDataX = new SysDataX();
        //表单Action指定
        sysDataX.setFormAction(prefix + "/add");
        mmap.put("sysDataX", sysDataX);
        return prefix + "/add";
    }

    /**
     * 新增保存Datax配置
     */
    @RequiresPermissions("tool:dataX:add")
    @Log(title = "Datax配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysDataX sysDataX, HttpServletRequest request, Model model) {
        if (StringUtils.isNotBlank(sysDataX.getId())) {
            //生成json文件
            DataXJsonCommon.dataxJsonMod(sysDataX);
            sysDataX.setLog("");
            return toAjax(sysDataXService.saveOrUpdate(sysDataX));
        } else {
            //生成json文件
            DataXJsonCommon.dataxJsonMod(sysDataX);
            return toAjax(sysDataXService.save(sysDataX));
        }

    }

    /**
     * 修改Datax配置
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        SysDataX sysDataX = sysDataXService.getById(id);
        //表单Action指定
        sysDataX.setFormAction(prefix + "/edit");
        //主键加密（配合editSave方法使用）- 如果需防止数据ID泄露，请放开，否则请删除此处代码
        //sysDataX.setId(pk_encrypt(sysDataX.getId()));

        mmap.put("sysDataX", sysDataX);
        return prefix + "/add";
    }

    /**
     * 修改保存Datax配置
     */
    @RequiresPermissions("tool:dataX:edit")
    @Log(title = "Datax配置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysDataX sysDataX) {

        //主键解密（配合edit方法使用，请确认edit方法中加密了）- 如果需防止数据ID泄露，请放开，否则请删除此处代码
        //sysDataX.setId(pk_decrypt(sysDataX.getId()));

        return toAjax(sysDataXService.saveOrUpdate(sysDataX));
    }

    /**
     * 删除Datax配置
     */
    @RequiresPermissions("tool:dataX:remove")
    @Log(title = "Datax配置", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        //删除对应的文件
        for (String id : Arrays.asList(ids.split(","))
        ) {
            //删除文件
            DataXJsonCommon.delJsonAndLog(sysDataXService.getById(id).getFileName());
        }
        return toAjax(sysDataXService.removeByIds(Arrays.asList(Convert.toStrArray(ids))));


    }

    /**
     * 文件名唯一验证
     *
     * @param oldFileName
     * @param fileName
     * @return
     */
    @PostMapping("/validateFileName")
    @ResponseBody
    public String validateFileName(@PathParam("oldFileName") String oldFileName, @PathParam("fileName") String fileName) {
        if (StringUtils.isNotEmpty(oldFileName) && oldFileName.equals(fileName)) {
            return "0";
        }
        if (sysDataXService.getOne(new QueryWrapper<SysDataX>().eq("file_name", fileName)) == null
        ) {
            return "0";
        }
        return "1";
    }

    @PostMapping("/syc")
    @ResponseBody
    public AjaxResult syc(String id, String fileName) {
        if (StrUtil.isNotBlank(fileName) && StrUtil.isNotBlank(id)) {
            SysDataX sysDataX = new SysDataX();
            sysDataX.setId(id);
            String log = DataXJsonCommon.exeDataX(fileName);
            sysDataX.setLog(log);
            return toAjax(sysDataXService.saveOrUpdate(sysDataX));
        } else {
            //文件名空返回失败
            return toAjax(false);
        }
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String id, ModelMap mmap, String showLog) {
        mmap.put("sysDataX", sysDataXService.getById(id));
        return prefix + "/detail";
    }
}
