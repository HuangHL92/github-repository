package com.ruoyi.quartz.task;

import com.ruoyi.web.websocket.SocketServer;
import org.springframework.stereotype.Component;

/**
 * 定时任务调度测试
 * 
 * @author ruoyi
 */
@Component("ryTask")
public class RyTask
{
    public void ryParams(String params)
    {
        System.out.println("执行有参方法：" + params);
        SocketServer.sendMessage("执行有参方法！","onlineNotice");
    }

    public void ryNoParams()
    {
        System.out.println("执行无参方法");
        SocketServer.sendMessage("执行无参方法！","onlineNotice");
    }
}
