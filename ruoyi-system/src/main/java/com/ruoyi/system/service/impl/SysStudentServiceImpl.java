package com.ruoyi.system.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysStudentMapper;
import com.ruoyi.system.domain.SysStudent;
import com.ruoyi.system.service.ISysStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;

/**
 * 这是一个学生 服务层实现
 *
 * @author jiyunsoft
 * @date 2019-08-20
 */
@Service
public class SysStudentServiceImpl extends ServiceImpl<SysStudentMapper, SysStudent> implements ISysStudentService
{
    @Override
    public List<SysStudent> selectList(SysStudent sysStudent) {
        QueryWrapper<SysStudent> query = new QueryWrapper<>();
        // 查询条件

        return list(query);
    }
}


