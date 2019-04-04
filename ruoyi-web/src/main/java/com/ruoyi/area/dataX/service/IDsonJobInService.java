package com.ruoyi.area.dataX.service;

import com.ruoyi.area.dataX.domain.DsonJobIn;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * Datax配置 服务层
 * 
 * @author jiyunsoft
 * @date 2019-04-02
 */
public interface IDsonJobInService extends IService<DsonJobIn>
{
    List<DsonJobIn> selectList(DsonJobIn dsonJobIn);
}
