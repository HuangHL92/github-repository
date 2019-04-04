package com.ruoyi.web.controller.demo;


import com.ruoyi.area.demo.domain.Demo;
import com.ruoyi.area.demo.service.IDemoService;
import com.ruoyi.framework.web.base.BaseController;
import com.ruoyi.system.service.ISysPostService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.page.TableDataInfo;

//
@Controller
@RequestMapping("/demo/resource")
public class ResourceController extends BaseController {

    private String prefix = "demo/resource";

    @Autowired
    private ISysPostService postService;

    @Autowired
    private IDemoService demoService;

    @GetMapping()
    public String demo() {
        return prefix + "/all";
    }

    /**
     * 查询测试列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Demo demo) {

        startPage();
        return getDataTable(demoService.selectList(demo));
    }

    /**
     * 新增测试
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        mmap.put("posts", postService.selectPostAll());
        Demo demo = new Demo();
        //表单Action指定
        demo.setFormAction(prefix + "/add");
        mmap.put("demo", demo);
        return prefix + "/add";
    }
}
