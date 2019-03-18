package com.ruoyi.framework.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.config.Global;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;

import java.io.File;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Json文件 工具类
 * 
 * @author ruoyi
 */
public class JsonFileUtils
{

    /**
     * 组织机构Json文件目录
     */
    public static String ORG_JSON_FILE_PATH = Global.getJsonPath() + "org.json";

    private static ISysUserService userService = SpringUtils.getBean(ISysUserService.class);
    private static ISysDeptService deptService = SpringUtils.getBean(ISysDeptService.class);


    /**
     * 获取组织机构json字符串
     * @return
     */
    public static String getOrgTreeJson()
    {
        String orgJson;
        // json文件存在，直接读取
        if (FileUtil.exist(ORG_JSON_FILE_PATH)) {
            orgJson = JSONUtil.readJSONObject(new File(FileUtil.getAbsolutePath(ORG_JSON_FILE_PATH)), Charset.forName("UTF-8")).toJSONString(4);
        }
        // json文件不存在，数据库读取json并生成json文件
        else {
            JSONObject result = new JSONObject();
            JSONArray array = new JSONArray();
            bindChildByParent(0L, array);
            result.put("data", array);
            orgJson = result.toJSONString(4);
            FileUtil.writeString(orgJson, ORG_JSON_FILE_PATH, "UTF-8");
        }
        return orgJson;
    }

    /**
     * 删除组织机构json文件
     * @return
     */
    public static void deleteOrgJsonFile()
    {
        FileUtil.del(ORG_JSON_FILE_PATH);
    }

    /**
     * 获取组织结构json
     * @param pId
     * @param array
     * @return
     */
    private static JSONArray bindChildByParent(Long pId, JSONArray array) {
        SysDept parent = new SysDept();
        List<SysDept> deptList;
        if (pId == 0) {
            deptList = deptService.selectTopList(parent);
        } else{
            parent.setParentId(pId);
            deptList = deptService.selectDeptList(parent);
        }
        for (SysDept item : deptList) {
            JSONObject object = new JSONObject(new LinkedHashMap());
            object.put("id", item.getDeptId()); // id
            object.put("deptname", item.getDeptName()); // deptname
            parent.setParentId(item.getDeptId());
            List<SysDept> childrenDept = deptService.selectDeptList(parent);
            JSONArray children = getUserJsonDeptId(item.getDeptId());
            if (childrenDept != null && childrenDept.size() > 0) {
                object.put("children", bindChildByParent(item.getDeptId(), children)); // children
            } else {
                object.put("children", children); // children
            }
            array.add(object);
        }
        return array;
    }

    /**
     * 根据部门id获取用户列表
     * @param deptId
     * @return
     */
    private static JSONArray getUserJsonDeptId(Long deptId) {
        JSONArray array = new JSONArray();
        SysUser sysUser = new SysUser();
        sysUser.setDeptId(deptId);
        List<SysUser> userList = userService.selectUserListByDeptId(sysUser);
        for (SysUser user : userList) {
            JSONObject object = new JSONObject(new LinkedHashMap());
            object.put("id", user.getUserId()); // id
            object.put("deptname", user.getUserName()); // deptname
            object.put("children", new JSONArray()); // children
            array.add(object);
        }
        return array;
    }
}
