package com.ruoyi.area.demo.mapper;
import com.ruoyi.area.demo.domain.SysPeople;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 人员 数据层
 *
 * @author jiyunsoft
 * @date 2019-08-21
 */
@Mapper
public interface SysPeopleMapper extends BaseMapper<SysPeople>
{

    List<SysPeople> selectPeopleAll();

    List<SysPeople> selectpeoplesByTableId(String tableId);
}
