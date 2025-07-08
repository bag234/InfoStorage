package org.mrbag.InfoStorage.Storge;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import io.lettuce.core.RedisURI;

@Configuration
public class RedisConfgStore {

	@SuppressWarnings("deprecation")
	@Bean(name = "MainStorageConfig")
	public RedisStandaloneConfiguration mainStorage(@Value("${app.data.mainURI}") String url) {
		RedisURI uri = RedisURI.create(url);
		RedisStandaloneConfiguration conf = new RedisStandaloneConfiguration(); 
		conf.setDatabase(uri.getDatabase());
		conf.setHostName(uri.getHost());
		conf.setPassword(uri.getPassword());
		conf.setPort(uri.getPort());
		conf.setUsername(uri.getUsername());
		return conf;
	}
	
	@Bean(name = "StandartClientConfig")
	@Deprecated
	public LettuceClientConfiguration configConnect() {
		return LettuceClientConfiguration.builder().useSsl().and()
				.commandTimeout(Duration.ofSeconds(4)).build();
	}
	
	@Bean(name= "mainConnect")
	public ReactiveRedisConnectionFactory configFactory(
			RedisStandaloneConfiguration uri, LettuceClientConfiguration config) {
		return new LettuceConnectionFactory(uri);
	}
	
	@Bean(name = "primaryTemplate")
	public ReactiveRedisTemplate<String, String> getTemplMain(ReactiveRedisConnectionFactory mainConnect){
		return new ReactiveRedisTemplate<String, String>(mainConnect, RedisSerializationContext.string());
	}
}
