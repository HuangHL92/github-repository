package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.Demo;
import java.util.List;	

/**
 * 测试 数据层
 * 
 * @author ruoyi
 * @date 2019-01-18
 */
public interface DemoMapper 
{
	/**
     * 查询测试信息
     * 
     * @param id 测试ID
     * @return 测试信息
     */
	public Demo selectDemoById(String id);
	
	/**
     * 查询测试列表
     * 
     * @param demo 测试信息
     * @return 测试集合
     */
	public List<Demo> selectDemoList(Demo demo);
	
	/**
     * 新增测试
     * 
     * @param demo 测试信息
     * @return 结果
     */
	public int insertDemo(Demo demo);
	
	/**
     * 修改测试
     * 
     * @param demo 测试信息
     * @return 结果
     */
	public int updateDemo(Demo demo);
	
	/**
     * 删除测试
     * 
     * @param id 测试ID
     * @return 结果
     */
	public int deleteDemoById(String id);
	
	/**
     * 批量删除测试
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteDemoByIds(String[] ids);
	
}