package com.ruoyi.web.controller.demo;

import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.web.websocket.SocketServer;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 测试 websocket测试
 *
 * @author yufei
 * @date 2019-2-22
 */
@Controller
@RequestMapping("/demo/websocket")
public class WebSocketController {
    @Autowired
    private SocketServer socketServer;

    
    @GetMapping()
    public String websocket()
    {
        return "demo/websocket/websocket";
    }


    @RequestMapping(value = "/client")
    public String idnex() {

        return "demo/websocket/client";
    }

    @RequestMapping(value = "/server")
    public String server(Model model) {
        int num = socketServer.getOnlineNum();
        String str = socketServer.getOnlineUsers();
        List<String> list = null;
        if (str.length() > 2) {
            str = str.substring(0,str.length()-1);
            String [] strs = str.split(",");
            list =  Arrays.asList(strs);
        }


        model.addAttribute("num",num);
        model.addAttribute("users",list);
        return "demo/websocket/server";
    }

    /**
     * 个人信息推送
     * @return
     */
    @RequestMapping("sendmsg")
    @ResponseBody
    public String sendmsg(String msg, String username){
        //第一个参数 :msg 发送的信息内容
        //第二个参数为用户长连接传的用户人数
        String [] persons = username.split(",");
        SocketServer.SendMany(msg,persons);
        return "success";
    }

    /**
     * 推送给所有在线用户
     * @return
     */
    @RequestMapping("sendAll")
    @ResponseBody
    public String sendAll(String msg){
        SocketServer.sendAll(msg);
        return "success";
    }

    /**
     * 获取当前在线用户
     * @return
     */
    @RequestMapping("webstatus")
    public String webstatus(){
        //当前用户个数
        int count = SocketServer.getOnlineNum();
        //当前用户的username
        SocketServer.getOnlineUsers();
        return "tongji";
    }
}