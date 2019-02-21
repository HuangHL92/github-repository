/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.ruoyi.framework.util;


import com.ruoyi.system.domain.SysUser;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Cache工具类
 * @author jeeplus
 * @version 2017-1-19
 */
public class CacheUtils {

	private CacheManager cacheManager;

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	private Cache<String, AtomicInteger> loginRecordCache;

	private Cache<String, SysUser> userCache;

	@PostConstruct
	public void init()
	{
		loginRecordCache = cacheManager.getCache(CacheType.LOGIN_RECORD_CACHE);
		userCache = cacheManager.getCache(CacheType.USER_CACHE);
	}

	public Cache<String, AtomicInteger> getLoginRecordCache() {
		return loginRecordCache;
	}

	public Cache<String, SysUser> getUserCache() {
		return userCache;
	}
}
