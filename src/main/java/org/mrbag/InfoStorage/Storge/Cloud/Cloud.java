package org.mrbag.InfoStorage.Storge.Cloud;

import java.time.LocalDateTime;

import org.mrbag.InfoStorage.Util.SimpleCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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
			boolean isSingle, TypeAccessPassword type, int days) throws CloudExceptionProcess {
		if (data == null || data.isBlank() || data.isEmpty() || days < -1)
			throw new CloudExceptionProcess("Data must not be null or days must not be less than 0");

		String token;
		int i = 0; 
		do {
			if (i++ > 25) {
				log.warn("Wrong key generation process");
				throw new CloudExceptionProcess("Wrong Key generator, try latter;");
			}
				
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
		} catch (CloudExceptionProcess e) {}
		return false;
	}

	public CloudKeyAccess checkInfoKey(String key) throws CloudExceptionProcess {
		
		try {
			if (key == null || key.isBlank())
				throw new CloudExceptionProcess("Key must not be null or Blank");
			if (!canExit(key))
				 return null;
			CloudKeyAccess data = CloudKeyAccess.parse(primaryTemplate.opsForValue().get("alias:" + key).block());
			if (data.getTime().isBefore(LocalDateTime.now())) {
				primaryTemplate.opsForValue().delete("alias:" + key).block();
				return null;
			}

			return data;
		}
		catch (CloudExceptionProcess e ) {
			throw e;
		}
		catch (Exception e) {
			log.error("App Store error message: ", e);
			throw e;
		}
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
		} catch (CloudExceptionProcess e) {}
		return false;
	}

	public String getDataFromAlias(
			String token, String password, TypeAccessPassword type) throws CloudExceptionProcess {
		CloudKeyAccess key = checkInfoKey(token);
		if (key == null || key.getType() != type || !password.equals(password))
			throw new CloudExceptionProcess("Key must not be null or key creditals must be correct");
		
		String data = load(key);

		if (key.isSingle())
			primaryTemplate.opsForValue().delete("alias:" + token).block();

		return data;
	}

}
