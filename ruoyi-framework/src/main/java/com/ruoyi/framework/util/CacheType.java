/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.ruoyi.framework.util;


import com.ruoyi.system.domain.SysUser;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Cache工具类
 * @author jeeplus
 * @version 2017-1-19
 */
@Component
public class CacheType {

	/**
	 * Redis缓存前缀，用于区分不同的项目
	 */
	private static final String SPRING_REDIS_PREFIX = "jy_basic:";

	/**
	 * session缓存
	 */
	public static final String SHIRO_ACTIVE_SESSION_CACHE = SPRING_REDIS_PREFIX + "shiro_redis_session";

	/**
	 * 登录记录缓存
	 */
	public static final String LOGIN_RECORD_CACHE = SPRING_REDIS_PREFIX + "loginRecordCache";

	/**
	 * shiro 认证缓存
	 */
	public static final String AUTHENTICATION_CACHE = SPRING_REDIS_PREFIX + "authenticationcache";
	/**
	 * shiro 授权缓存
	 */
	public static final String AUTHORIZATION_CACHE = SPRING_REDIS_PREFIX + "authorizationcache";
	/**
	 * 用户 缓存
	 */
	public static final String USER_CACHE = SPRING_REDIS_PREFIX + "userCache";
}
