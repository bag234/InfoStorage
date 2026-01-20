package org.mrbag.InfoStorage.Storge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.NonNull;

@Service
public class Store {

	@Autowired
	RedisTemplate<KeyStore, String> temp;

	public boolean canExit(KeyStore key) {
		if (key == null || key.canStore() || !key.isValid())
			return false;
		return temp.opsForValue().get(key) != null;
	}

	public KeyStore save(String password, String data) {
		if (password == null || data == null || password.isEmpty() || data.isEmpty())
			throw new NullPointerException("Data or password is empty");
		KeyStore key = KeyStore.builder().password(password).build().generateId();
		// TODO Exit
		while (canExit(key))
			key.generateId();

		temp.opsForValue().set(key, data);

		return key;
	}

	public String load(@NonNull KeyStore key) {
		return temp.opsForValue().get(key);
	}

}
