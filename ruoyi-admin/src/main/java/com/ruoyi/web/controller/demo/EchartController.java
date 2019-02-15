package com.ruoyi.web.controller.demo;

import com.ruoyi.framework.web.base.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * echart实例demo
 *
 * @author ruoyi
 */
@Controller
@RequestMapping("/demo/echart")
public class EchartController extends BaseController {

    private String prefix = "demo";

    @GetMapping()
    public String echart()
    {
        return prefix + "/echart";
    }


}
