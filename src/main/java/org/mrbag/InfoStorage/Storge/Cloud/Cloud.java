package org.mrbag.InfoStorage.Storge.Cloud;

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
			boolean isSingle, TypeAccessPassword type, int days) throws Exception {
		if (data == null || data.isBlank() || data.isEmpty() || days < -1)
			throw new Exception("Data must not be null or days must not be less than 0");

		String token;

		do {
			token = SimpleCodeGenerator.generateInt();
		} while (!canFreeAlias(token));

		CloudKeyAccess key = CloudKeyAccess.builder()
				.password(password != null ? password : "")
				.isSingle(isSingle)
				.type(type != null ? type : TypeAccessPassword.NONE)
				.key(token)
				.time(LocalDateTime.now().plusDays(days))
				.build();

		primaryTemplate.opsForValue().set(key.toStringAccessKey(), data).block();
		primaryTemplate.opsForValue().set(key.keyAlias(), key.contentAlias()).block();

		return key;
	}

	public boolean canFreeAlias(String key) {
		CloudKeyAccess check;
		try {
			check = checkInfoKey(key);
			if (check != null) {
				if (check.getTime().isBefore(LocalDateTime.now())) {
					primaryTemplate.opsForValue().delete("alias:" + key).block();
					return true;
				}
				return false;
			}
			return true;
		} catch (Exception e) {}
		return false;
	}

	public CloudKeyAccess checkInfoKey(String key) throws Exception {
		if (key == null || key.isBlank() || !canExit(key))
			throw new Exception("Key must not be null or Blank");

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
	
    public boolean delete(String token, String password, TypeAccessPassword type)  {
    	CloudKeyAccess key;
		try {
			key = checkInfoKey(token);
			if (key == null || key.getType() != type || !password.equals(password))
				return false;
			
			primaryTemplate.opsForValue().delete("alias:" + token).block();
			return true;
		} catch (Exception e) {}
		return false;
	}

	public String getDataFromAlias(
			String token, String password, TypeAccessPassword type) throws Exception {
		CloudKeyAccess key = checkInfoKey(token);
		if (key == null || key.getType() != type || !password.equals(password))
			throw new Exception("Key must not be null or key creditals must be correct");
		
		String data = load(key);

		if (key.isSingle())
			primaryTemplate.opsForValue().delete("alias:" + token).block();

		return data;
	}

}
