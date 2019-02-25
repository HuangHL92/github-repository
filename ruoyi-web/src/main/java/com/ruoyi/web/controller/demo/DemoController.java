package com.ruoyi.web.controller.demo;

import java.awt.*;
import java.io.IOException;
import java.util.List;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageHelper;
import com.google.gson.JsonObject;
import com.ruoyi.common.utils.JedisUtils;
import com.ruoyi.demo.domain.Demo;
import com.ruoyi.demo.service.IDemoService;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysPostService;
import com.ruoyi.system.service.ISysUserService;
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
@RequestMapping("/demo/all")
public class DemoController extends BaseController
{
    private String prefix = "demo/all";
	
	@Autowired
	private IDemoService demoService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysPostService postService;
	
	@RequiresPermissions("demo:all:view")
	@GetMapping()
	public String demo()
	{
	    return prefix + "/all";
	}
	
	/**
	 * 查询测试列表
	 */
	@RequiresPermissions("demo:all:list")
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
	@RequiresPermissions("demo:all:export")
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
	public String add(ModelMap mmap)
	{
        mmap.put("posts", postService.selectPostAll());
        return prefix + "/add";
	}
	
	/**
	 * 新增保存测试
	 */
	@RequiresPermissions("demo:all:add")
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
	@RequiresPermissions("demo:all:edit")
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
	@RequiresPermissions("demo:all:remove")
	@Log(title = "测试", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(demoService.deleteDemoByIds(ids));
	}


    /**
     * 发送邮件
     */
    @Log(title = "发送邮件", businessType = BusinessType.OTHER)
    @PostMapping( "/sendMail")
    @ResponseBody
    public AjaxResult sendMail(HttpServletRequest request)
    {
        String mailto = StringUtils.isEmpty(request.getParameter("mailto"))?"fei.yu@51e.com.cn":request.getParameter("mailto");
        String mailtext = StringUtils.isEmpty(request.getParameter("mailtext"))?"邮件来自Hutool测试":request.getParameter("mailtext");
        MailUtil.send(mailto, "测试", mailtext, false);

        return toAjax(1);
    }


    /**
     * 生成二维码
     */
    @Log(title = "生成二维码", businessType = BusinessType.OTHER)
    @PostMapping( "/createQRCode")
    @ResponseBody
    public AjaxResult createQRCode(HttpServletRequest request)
    {

        //自定义配置
        QrConfig config = new QrConfig(300, 300);
        // 设置边距，既二维码和背景之间的边距
        config.setMargin(3);
        // 设置前景色，既二维码颜色（青色）
        config.setForeColor(Color.CYAN.getRGB());
        // 设置背景色（灰色）
        config.setBackColor(Color.GRAY.getRGB());
        //二维码内容
        String qrcode = StringUtils.isEmpty(request.getParameter("qrcode"))?"http://www.baidu.com":request.getParameter("qrcode");
        // 生成二维码到文件，也可以到流
        QrCodeUtil.generate(qrcode, config, FileUtil.file("/ruoyi/二维码.jpg"));

        // 默认输出
        //QrCodeUtil.generate(qrcode, 300,300, FileUtil.file("d:/ruoyi/二维码.jpg"));

        return toAjax(1);
    }


    /**
     * 识别二维码
     */
    @Log(title = "识别二维码", businessType = BusinessType.OTHER)
    @GetMapping( "/checkQRCode")
    @ResponseBody
    public AjaxResult checkQRCode(HttpServletRequest request)
    {

        String decode = QrCodeUtil.decode(FileUtil.file("/ruoyi/二维码.jpg"));

        return AjaxResult.success(decode);
    }


    /**
     * redis存入操作
     */
    @Log(title = "redis存入操作", businessType = BusinessType.OTHER)
    @PostMapping( "/saveRedis")
    @ResponseBody
    public AjaxResult saveRedis(HttpServletRequest request)
    {

		String key = request.getParameter("key");
		String value = request.getParameter("value");
		if (StringUtils.isAnyBlank(key, value)) {
			return error("请输入key和value!");
		}
		// 默认15s过期；0为不过期
		JedisUtils.set(key, value, 15);
		return success();
    }


    /**
     * redis获取操作
     */
    @Log(title = "redis获取操作", businessType = BusinessType.OTHER)
    @GetMapping( "/getRedis/{key}")
    @ResponseBody
    public AjaxResult getRedis(@PathVariable("key") String key)
    {
		String value = JedisUtils.get(key);
		if (StringUtils.isBlank(value)) {
			return error("该key对应的值不存在或者已过期");
		}
        return success(value);
    }


    @GetMapping( "/getJson/{name}")
    @ResponseBody
    public JsonObject getJson(@PathVariable("name") String name)
    {
        SysUser user = new SysUser();
        user.setUserName(name);
        List<SysUser> users = userService.selectUserList(user);


        JsonObject json = new JsonObject();
        json.addProperty("code", 0);
        json.addProperty("redirect", "");
        json.addProperty("message", "");
        json.addProperty("value", "");

        return json;
    }

}
