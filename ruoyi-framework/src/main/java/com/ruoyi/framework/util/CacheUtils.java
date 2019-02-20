/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.ruoyi.framework.util;


import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Cache工具类
 * @author jeeplus
 * @version 2017-1-19
 */
@Component
public class CacheUtils {

	@Autowired
	private CacheManager cacheManager;

	public Cache<String, AtomicInteger> loginRecordCache;

	@PostConstruct
	public void init()
	{
		loginRecordCache = cacheManager.getCache("loginRecordCache");
	}

}
