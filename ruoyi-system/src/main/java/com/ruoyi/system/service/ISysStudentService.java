package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysStudent;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * 这是一个学生 服务层
 *
 * @author jiyunsoft
 * @date 2019-08-20
 */
public interface ISysStudentService extends IService<SysStudent>
{
    List<SysStudent> selectList(SysStudent sysStudent);
}
