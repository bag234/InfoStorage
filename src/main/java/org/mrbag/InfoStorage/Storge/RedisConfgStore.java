package org.mrbag.InfoStorage.Storge;

import org.mrbag.InfoStorage.Storge.Cloud.CloudKeyStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.lettuce.core.RedisURI;

@Configuration
public class RedisConfgStore {

	@SuppressWarnings("deprecation")
	@Bean(name = "MainStorageConfig")
	public RedisStandaloneConfiguration mainStorage(@Value("${app.data.mainuri}") String url) {
		RedisURI uri = RedisURI.create(url);
		RedisStandaloneConfiguration conf = new RedisStandaloneConfiguration();
		conf.setDatabase(uri.getDatabase());
		conf.setHostName(uri.getHost());
		conf.setPassword(uri.getPassword());
		conf.setPort(uri.getPort());
		conf.setUsername(uri.getUsername());
		return conf;
	}

	@Bean
	public LettuceConnectionFactory getConnection(RedisStandaloneConfiguration config) {
		LettuceConnectionFactory let = new LettuceConnectionFactory(config);
		let.start();
		return let;
	}

	@Bean("redisTemplate")
	public RedisTemplate<String, String> redisTemplate(LettuceConnectionFactory let) {
		RedisTemplate<String, String> temp = new RedisTemplate<>();
		temp.setConnectionFactory(let);

		return temp;
	}

	@Bean
	public RedisTemplate<KeyStore, String> getTemplateKeyStore(LettuceConnectionFactory let) {
		RedisTemplate<KeyStore, String> temp = new RedisTemplate<>();
		temp.setConnectionFactory(let);
		temp.setKeySerializer(new Jackson2JsonRedisSerializer<>(KeyStore.class));
		temp.setValueSerializer(new StringRedisSerializer());
		return temp;
	}

	public ObjectMapper getMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return objectMapper;
	}

	@Bean
	public RedisTemplate<CloudKeyStore, String> getTemplateCloudKeyStore(LettuceConnectionFactory let) {
		RedisTemplate<CloudKeyStore, String> temp = new RedisTemplate<>();
		temp.setConnectionFactory(let);
		temp.setKeySerializer(new Jackson2JsonRedisSerializer<>(getMapper(), CloudKeyStore.class));
		temp.setValueSerializer(new StringRedisSerializer());

		return temp;
	}

	@Bean
	public RedisTemplate<String, CloudKeyStore> getTemplateAliasCloudKeyStore(LettuceConnectionFactory let) {
		RedisTemplate<String, CloudKeyStore> temp = new RedisTemplate<>();
		temp.setConnectionFactory(let);
		temp.setKeySerializer(new StringRedisSerializer());
		temp.setValueSerializer(new Jackson2JsonRedisSerializer<>(getMapper(), CloudKeyStore.class));
		return temp;
	}
}
