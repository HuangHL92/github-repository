package com.ruoyi.quartz.task;

import cn.hutool.core.date.DateTime;
import com.ruoyi.web.websocket.SocketServer;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.stereotype.Component;

@Component("demoTask")
public class demoTask {

    public void sendMail() throws InterruptedException {
//        String mailto = "15800822996@163.com";
//        String mailtext = "定时任务测试";
//        MailUtil.send(mailto, "测试", mailtext, false);
        SocketServer.sendMessage("定时任务测试！" + DateTime.now(),"onlineNotice");

        //TODO 模拟程序长时间处理
//        int index =0;
//        while(true) {
//            Thread.currentThread().sleep(500);
//            index++;
//            if(index>200) {
//                break;
//            }
//        }
//        int i =1/2;

    }

}
