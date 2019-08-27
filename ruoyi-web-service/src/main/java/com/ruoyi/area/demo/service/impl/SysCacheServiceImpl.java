package com.ruoyi.area.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import com.ruoyi.area.demo.mapper.SysCacheMapper;
import com.ruoyi.area.demo.domain.SysCache;
import com.ruoyi.area.demo.service.SysCacheService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;

/**
 * div 服务层实现
 *
 * @author jiyunsoft
 * @date 2019-08-20
 */
@Service
public class SysCacheServiceImpl extends ServiceImpl<SysCacheMapper, SysCache> implements SysCacheService {
    @Override
    public List<SysCache> selectList(SysCache sysCache) {
        QueryWrapper<SysCache> query = new QueryWrapper<>();
        // 查询条件
        return list(query);
    }
}