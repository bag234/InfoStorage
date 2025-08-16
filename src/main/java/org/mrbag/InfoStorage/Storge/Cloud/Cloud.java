package org.mrbag.InfoStorage.Storge.Cloud;

import java.lang.module.ModuleDescriptor.Builder;
import java.time.LocalDateTime;

import org.mrbag.InfoStorage.Util.SimpleCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;


@Service
public class Cloud {

	@Autowired
	ReactiveRedisTemplate<String, String> primaryTemplate; 
	
	public boolean canExit(CloudKeyAccess key) {
		return primaryTemplate.hasKey(key.toStringAccessKey()).block();
	}
	
	public boolean canExit(String key) {
		return primaryTemplate.hasKey("alias:" + key).block();
	}
	
	public CloudKeyAccess save(
			String data, String password, 
			boolean isSingle, TypeAccessPassword type, String days) {
		if (data == null) return null;
		
		String token;
		
		do {
			token = SimpleCodeGenerator.generateInt();
		} while(!canFreeAlias(token));
		
		CloudKeyAccess key = CloudKeyAccess.builder()
				.password(password != null ? password : "")
				.isSingle(isSingle)
				.type(type != null ? type : TypeAccessPassword.NONE)
				.key(token)
				.time(LocalDateTime.now().plusDays(Integer.parseInt(days)))
				.build();
		
		primaryTemplate.opsForValue().set(key.toStringAccessKey(), data).block();
		primaryTemplate.opsForValue().set(key.keyAlias(), key.contentAlias()).block();
		
		return key;
	}
	
	public boolean canFreeAlias(String key) {
		CloudKeyAccess check = checkInfoKey(key);
		if (check != null) {
			//add check date
			return false;
		}
		
		return true;
	}
	
	public CloudKeyAccess checkInfoKey(String key) {
		if (key == null || key.isBlank() || !canExit(key)) return null;
		
		CloudKeyAccess data = CloudKeyAccess.parse(primaryTemplate.opsForValue().get("alias:" + key).block());
		
		if (data.getTime().isBefore(LocalDateTime.now())) {
			primaryTemplate.opsForValue().delete("alias:" + key).block();
			return null;
		}
		
		return data;
		
	}
	
	public String load(CloudKeyAccess key) {
		if (key != null) 
			return primaryTemplate.opsForValue().get(key.toStringAccessKey()).block();
		return null;
	}
	
	public String getDataFromAlias(
			String token, String password, TypeAccessPassword type
			) {
		CloudKeyAccess key = checkInfoKey(token);
		if (key == null || key.getType() != type || !password.equals(password))
			return null; 
		
		String data = load(key); 
		
		if (key.isSingle())
			primaryTemplate.opsForValue().delete("alias:" + token).block();
		
		return data;
	}
	
}
