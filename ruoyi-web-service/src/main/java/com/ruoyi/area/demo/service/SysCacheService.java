package com.ruoyi.area.demo.service;

import com.ruoyi.area.demo.domain.SysCache;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * div 服务层
 *
 * @author jiyunsoft
 * @date 2019-08-20
 */
public interface SysCacheService extends IService<SysCache>
{
    List<SysCache> selectList(SysCache sysCache);
}
