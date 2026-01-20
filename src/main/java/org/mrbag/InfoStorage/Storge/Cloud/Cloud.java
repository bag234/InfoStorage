package org.mrbag.InfoStorage.Storge.Cloud;

import java.time.LocalDateTime;

import org.mrbag.InfoStorage.Util.SimpleCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class Cloud {

	@Autowired
	RedisTemplate<CloudKeyStore, String> temp;

	@Autowired
	RedisTemplate<String, CloudKeyStore> alias;

	public boolean canExit(CloudKeyStore key) {
		return temp.hasKey(key);
	}

	public boolean canExit(String key) {
		return alias.hasKey(key);
	}

	public CloudKeyStore save(
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

		CloudKeyStore key = CloudKeyStore.builder()
				.password(password != null ? password : "")
				.isSingle(isSingle)
				.type(type != null ? type : TypeAccessPassword.NONE)
				.key(token)
				.time(LocalDateTime.now().plusDays(days))
				.build();

		temp.opsForValue().set(key, data);
		alias.opsForValue().set(key.getKey(), key);

		return key;
	}

	public boolean canFreeAlias(String key) {
		CloudKeyStore check;
		try {
			check = checkInfoKey(key);
			if (check != null) {
				if (check.getTime().isBefore(LocalDateTime.now())) {
					alias.delete(key);
					return true;
				}
				return false;
			}
			return true;
		} catch (CloudExceptionProcess e) {
		}
		return false;
	}

	public CloudKeyStore checkInfoKey(String key) throws CloudExceptionProcess {
		try {
			if (key == null || key.isBlank())
				throw new CloudExceptionProcess("Key must not be null or Blank");
			if (!canExit(key))
				return null;
			CloudKeyStore data = alias.opsForValue().get(key);
			if (data.getTime().isBefore(LocalDateTime.now())) {
				alias.delete(key);
				return null;
			}

			return data;
		} catch (CloudExceptionProcess e) {
			throw e;
		} catch (Exception e) {
			log.error("App Store error message: ", e);
			throw e;
		}
	}

	public String load(CloudKeyStore key) {
		if (key != null)
			return temp.opsForValue().get(key);
		return null;
	}

	public boolean delete(String token, String password, TypeAccessPassword type) {
		CloudKeyStore key;
		try {
			key = checkInfoKey(token);
			if (key == null || key.getType() != type || !password.equals(password))
				return false;

			alias.delete(token);
			return true;
		} catch (CloudExceptionProcess e) {
		}
		return false;
	}

	public String getDataFromAlias(
			String token, String password, TypeAccessPassword type) throws CloudExceptionProcess {
		CloudKeyStore key = checkInfoKey(token);
		if (key == null || key.getType() != type || !password.equals(password))
			throw new CloudExceptionProcess("Key must not be null or key creditals must be correct");

		String data = load(key);

		if (key.isSingle())
			alias.delete(token);

		return data;
	}

}
