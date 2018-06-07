/*package com.galaxyinternet.framework.core.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import com.galaxyinternet.framework.cache.Cache;
import com.galaxyinternet.framework.core.constants.Constants;

*//**
 * 
 *
 * @Description: 此类用于集成springsession，在分布式系统中保持各个子系统的session同步
 * @author keifer
 * @date 2016年3月12日
 *
 *//*
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 7200)
@Configuration
public class RedisHttpSessionConfig {

	@Autowired
	Cache cache;

	@Bean
	public RedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory connectionFactory = new JedisConnectionFactory(
				cache.getJedis().getShardInfo(Constants.REDIS_SHARDINFO_NAME));
		return connectionFactory;
	}
}
*/