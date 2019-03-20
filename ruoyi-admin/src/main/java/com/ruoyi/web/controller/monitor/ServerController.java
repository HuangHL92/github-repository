package com.ruoyi.web.controller.monitor;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.utils.http.HttpUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ruoyi.framework.web.base.BaseController;
import com.ruoyi.framework.web.domain.Server;

import javax.servlet.http.HttpServletRequest;

/**
 * 服务器监控
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/monitor/server")
public class ServerController extends BaseController
{
    private String prefix = "monitor/server";

    @Value("${management.server.port}")
    private String port;
    @RequiresPermissions("monitor:server:view")
    @GetMapping()
    public String server(ModelMap mmap, HttpServletRequest request) throws Exception
    {
        String jsonObj = HttpUtil.get(request.getScheme()+"://"+request.getServerName()+":"+port+"/actuator/health");
        JSONObject health = JSONUtil.parseObj(jsonObj);

        Server server = new Server();
        server.copyTo();
        mmap.put("server", server);
        mmap.put("health",health);
        return prefix + "/server";
    }
}
