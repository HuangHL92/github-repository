package com.ruoyi.api;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by RJ-001 on 2017-06-13.
 * API接口入口
 */
@Controller
public class ApiIndexController {


    @RequestMapping(value ={"/index",""} )
    @ApiIgnore
    public void index(HttpServletResponse response) throws IOException {
        response.sendRedirect("doc.html");
    }


}
