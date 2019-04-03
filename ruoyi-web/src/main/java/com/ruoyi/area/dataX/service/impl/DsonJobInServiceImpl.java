package com.ruoyi.area.dataX.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.area.dataX.domain.DsonJobIn;
import com.ruoyi.area.dataX.mapper.DsonJobInMapper;
import com.ruoyi.area.dataX.service.IDsonJobInService;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;

/**
 * Datax配置 服务层实现
 * 
 * @author jiyunsoft
 * @date 2019-04-02
 */
@Service
public class DsonJobInServiceImpl extends ServiceImpl<DsonJobInMapper, DsonJobIn> implements IDsonJobInService
{
    @Override
    public List<DsonJobIn> selectList(DsonJobIn dsonJobIn) {
        QueryWrapper<DsonJobIn> query = new QueryWrapper<>();
        // 查询条件
        String keyword = dsonJobIn.getParams().isEmpty()?null:dsonJobIn.getParams().get("keyword").toString();
        query.lambda().and(StrUtil.isNotBlank(keyword),i->i.like(DsonJobIn::getFileName,keyword));
        return list(query);
    }
}
