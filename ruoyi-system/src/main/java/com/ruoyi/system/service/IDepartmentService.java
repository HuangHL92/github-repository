package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.Department;

import java.util.List;

/**
 * 练习_部门 服务层
 * 
 * @author jiyunsoft
 * @date 2019-02-14
 */
public interface IDepartmentService extends IService<Department>
{
	List<Department> selectList(Department department);
}
