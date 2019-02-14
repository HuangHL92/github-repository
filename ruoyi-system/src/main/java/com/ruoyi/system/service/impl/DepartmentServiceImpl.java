package com.ruoyi.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.DepartmentMapper;
import com.ruoyi.system.domain.Department;
import com.ruoyi.system.service.IDepartmentService;

import java.util.List;

/**
 * 练习_部门 服务层实现
 * 
 * @author jiyunsoft
 * @date 2019-02-14
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService
{

    @Override
    public List<Department> selectList(Department department) {
        QueryWrapper<Department> query = new QueryWrapper<>();


        return list(query);
    }
}
