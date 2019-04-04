package com.ruoyi.web.controller.dataX;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.area.dataX.domain.DsonJobIn;
import com.ruoyi.area.dataX.service.IDsonJobInService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.jsonIO.DataXJsonCommon;
import com.ruoyi.common.page.TableDataInfo;
import com.ruoyi.common.support.Convert;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.base.BaseController;
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
@RequestMapping("dsonJobIn")
public class DsonJobInController extends BaseController {
    private String prefix = "dataX";

    @Autowired
    private IDsonJobInService dsonJobInService;

    @RequiresPermissions("dsonJobIn:view")
    @GetMapping()
    public String dsonJobIn() {
        return prefix + "/list";
    }

    /**
     * 查询Datax配置列表
     */
    @RequiresPermissions("dsonJobIn:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(DsonJobIn dsonJobIn) {
        startPage();
        return getDataTable(dsonJobInService.selectList(dsonJobIn));
    }


    /**
     * 导出Datax配置列表
     */
    @RequiresPermissions("dsonJobIn:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(DsonJobIn dsonJobIn) {
        List<DsonJobIn> list = dsonJobInService.selectList(dsonJobIn);
        ExcelUtil<DsonJobIn> util = new ExcelUtil<DsonJobIn>(DsonJobIn.class);
        return util.exportExcel(list, "dsonJobIn");
    }

    /**
     * 新增Datax配置
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        DsonJobIn dsonJobIn = new DsonJobIn();
        //表单Action指定
        dsonJobIn.setFormAction(prefix + "/add");
        mmap.put("dsonJobIn", dsonJobIn);
        return prefix + "/add";
    }

    /**
     * 新增保存Datax配置
     */
    @RequiresPermissions("dsonJobIn:add")
    @Log(title = "Datax配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(DsonJobIn dsonJobIn, HttpServletRequest request, Model model) {
        if (StringUtils.isNotBlank(dsonJobIn.getId())) {
            //生成json文件
            DataXJsonCommon.dataxJsonMod(dsonJobIn);
            dsonJobIn.setLog("");
            return toAjax(dsonJobInService.saveOrUpdate(dsonJobIn));
        } else {
            //生成json文件
            DataXJsonCommon.dataxJsonMod(dsonJobIn);
            return toAjax(dsonJobInService.save(dsonJobIn));
        }

    }

    /**
     * 修改Datax配置
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        DsonJobIn dsonJobIn = dsonJobInService.getById(id);
        //表单Action指定
        dsonJobIn.setFormAction(prefix + "/edit");
        //主键加密（配合editSave方法使用）- 如果需防止数据ID泄露，请放开，否则请删除此处代码
        //dsonJobIn.setId(pk_encrypt(dsonJobIn.getId()));

        mmap.put("dsonJobIn", dsonJobIn);
        return prefix + "/add";
    }

    /**
     * 修改保存Datax配置
     */
    @RequiresPermissions("dsonJobIn:edit")
    @Log(title = "Datax配置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(DsonJobIn dsonJobIn) {

        //主键解密（配合edit方法使用，请确认edit方法中加密了）- 如果需防止数据ID泄露，请放开，否则请删除此处代码
        //dsonJobIn.setId(pk_decrypt(dsonJobIn.getId()));

        return toAjax(dsonJobInService.saveOrUpdate(dsonJobIn));
    }

    /**
     * 删除Datax配置
     */
    @RequiresPermissions("dsonJobIn:remove")
    @Log(title = "Datax配置", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        //删除对应的文件
        for (String id : Arrays.asList(ids.split(","))
        ) {
            //删除文件
            DataXJsonCommon.delJsonAndLog(dsonJobInService.getById(id).getFileName());
        }
        return toAjax(dsonJobInService.removeByIds(Arrays.asList(Convert.toStrArray(ids))));


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
        if (dsonJobInService.getOne(new QueryWrapper<DsonJobIn>().eq("file_name", fileName)) == null
        ) {
            return "0";
        }
        return "1";
    }

    @PostMapping("/syc")
    @ResponseBody
    public AjaxResult syc(String id, String fileName) {
        if (StrUtil.isNotBlank(fileName) && StrUtil.isNotBlank(id)) {
            DsonJobIn dsonJobIn = new DsonJobIn();
            dsonJobIn.setId(id);
            String log = DataXJsonCommon.exeDataX(fileName);
            dsonJobIn.setLog(log);
            return toAjax(dsonJobInService.saveOrUpdate(dsonJobIn));
        } else {
            //文件名空返回失败
            return toAjax(false);
        }
    }

}
