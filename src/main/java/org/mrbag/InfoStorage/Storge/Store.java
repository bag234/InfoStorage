package org.mrbag.InfoStorage.Storge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class Store {

	@Autowired
	ReactiveRedisTemplate<String, String> primaryTemplate; 
	
	public boolean canExit(KeyAccess key) {
		if(key == null || key.canStore() || !key.isValid())
			return false;
		return primaryTemplate.hasKey(key.toString()).block();
	}
	
	public KeyAccess save(String password, String data) {
		if(password == null || data == null || password.isEmpty() || data.isEmpty()) 
			throw new NullPointerException("Data is empty");
		KeyAccess key = KeyAccess.builder().password(password).build().generateId();
		
		while(canExit(key)) key.generateId();
		
		primaryTemplate.opsForValue().set(key.toString(), data).block();
		
		return key;
	}
	
	public String load(KeyAccess key) {
		if (key == null ) return "";
		return primaryTemplate.opsForValue().get(key.toString()).block();
	}
	
}
