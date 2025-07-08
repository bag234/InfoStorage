package org.mrbag.InfoStorage.Storge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class Store {

	@Autowired
	RedisTemplate<String, String> primaryTemplate; 
	
	public boolean canExit(KeyAccess key) {
		if(key == null || key.canStore() || !key.isValid())
			return false;
		return primaryTemplate.hasKey(key.toString());
	}
	
	public KeyAccess save(String password, String data) {
		if(password.isEmpty() || data.isEmpty()) 
			throw new NullPointerException("Data is empty");
		KeyAccess key = KeyAccess.builder().password(password).build().generateId();
		primaryTemplate.opsForValue().set(key.toString(), data);
		
		return key;
	}
	
	public String load(KeyAccess key) {
		if (key == null ) return "";
		return primaryTemplate.opsForValue().get(key.toString());
	}
	
}
