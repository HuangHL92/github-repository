package com.ruoyi.system.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.system.domain.SysBook;
import com.ruoyi.system.mapper.SysBookMapper;
import com.ruoyi.system.service.ISysBookService;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;

/**
 * 用户 服务层实现
 *
 * @author jiyunsoft
 * @date 2019-08-19
 */
@Service
public class ISysBookServiceImpl extends ServiceImpl<SysBookMapper, SysBook> implements ISysBookService
{


    @Override
    public List<SysBook> selectList(SysBook sysUser) {
        QueryWrapper<SysBook> query = new QueryWrapper<>();
        // 查询条件

        return list(query);
    }
}

