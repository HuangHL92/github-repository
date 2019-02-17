package com.ruoyi.web.controller.system;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.common.config.Global;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.web.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@Controller
@RequestMapping("/system/upload")
public class FileUploadController extends BaseController {

    /***
     * 单个文件上传
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value="/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult uploadFile(@RequestParam("file") MultipartFile file,
                                HttpServletRequest request) {

        String contentType = file.getContentType();   //图片文件类型
        String fileName = file.getOriginalFilename();  //图片名字

        //文件存放路径
        String filePath = Global.getProfile();

        String path = "";
        //调用文件处理类FileUtil，处理文件，将文件写入指定位置
        try {
            path=  FileUploadUtils.upload(filePath,file,fileName);
        } catch (Exception e) {
            // TODO: handle exception
            return error(e.getMessage());
        }

        // 返回图片的存放路径
        return AjaxResult.success(path);
    }


    /***
     * 多个文件上传
     * @param files
     * @param request
     * @return
     */
    @RequestMapping(value="/uploadFiles", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult uploadFiles(@RequestParam("file") MultipartFile[] files,
                                 HttpServletRequest request) {

        List attids =  new ArrayList();
        for (MultipartFile file: files) {
            String contentType = file.getContentType();   //图片文件类型
            String fileName = file.getOriginalFilename();  //图片名字

            //文件存放路径
            String filePath = Global.getProfile();

            //调用文件处理类FileUtil，处理文件，将文件写入指定位置
            try {
                // 上传物理文件
                String path = FileUploadUtils.upload(filePath,file,fileName);
                //TODO 插入附件表
                attids.add(IdUtil.randomUUID());
            } catch (Exception e) {
                // TODO: handle exception
                return error(e.getMessage());
            }
        }

        // 返回附件编号
        AjaxResult json = new AjaxResult();
        json.put("attno", RandomUtil.randomUUID());
        json.put("attids", JSONUtil.parseArray(attids));
        json.put("code", 0);

        return json;
    }



    /***
     * 删除附件
     * @param attno 附件编号
     * @param request
     * @return
     */
    @RequestMapping(value="/delFiles", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delFiles(String attno,
                                  HttpServletRequest request) {

        try {
            //TODO 从附件表取得所有的附件数据（循环删除文件）

            //TODO 根据attno删除附件表

        } catch (Exception e) {
            // TODO: handle exception
            return error(e.getMessage());
        }

        return AjaxResult.success();
    }


    /***
     * 删除附件中的当个文件
     * @param attid 附件ID
     * @param request
     * @return
     */
    @RequestMapping(value="/delFile", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delFile(String attid,
                               HttpServletRequest request) {

        try {
            //TODO 根据AttID删除附件表
            //TODO 删除物理文件

        } catch (Exception e) {
            // TODO: handle exception
            return error(e.getMessage());
        }

        return AjaxResult.success();
    }


    /***
     * 根据文件路径删除文件
     * @param filePath 文件路径
     * @return
     */
    @RequestMapping(value="/removeFile", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult removeFile(String filePath) {

        try {
            //删除物理文件

        } catch (Exception e) {
            // TODO: handle exception
            return error(e.getMessage());
        }

        return AjaxResult.success();
    }


}
