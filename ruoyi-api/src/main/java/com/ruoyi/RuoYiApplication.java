package com.ruoyi;

import com.ruoyi.framework.config.ShiroConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * 启动程序
 * 
 * @author ruoyi
 */

@SpringBootApplication( exclude = {DataSourceAutoConfiguration.class})
public class RuoYiApplication
{
    public static void main(String[] args)
    {

        SpringApplication.run(RuoYiApplication.class, args);
        System.out.println("恭喜您！  ruoyi-api 启动成功    \n" +
                " .-------.       ____     __        \n" +
                " |  _ _   \\      \\   \\   /  /    \n" +
                " | ( ' )  |       \\  _. /  '       \n" +
                " |(_ o _) /        _( )_ .'         \n" +
                " | (_,_).' __  ___(_ o _)'          \n" +
                " |  |\\ \\  |  ||   |(_,_)'         \n" +
                " |  | \\ `'   /|   `-'  /           \n" +
                " |  |  \\    /  \\      /           \n" +
                " ''-'   `'-'    `-..-'              ");

    }


    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 注意这里要指向原先用main方法执行的Application启动类
        return builder.sources(RuoYiApplication.class);
    }


}