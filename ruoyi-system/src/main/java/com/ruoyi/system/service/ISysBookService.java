package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.SysBook;


import java.util.List;

/**
 * 用户 服务层
 *
 * @author jiyunsoft
 * @date 2019-08-19
 */
public interface ISysBookService extends IService<SysBook>
{
    List<SysBook> selectList(SysBook sysBook);
}
