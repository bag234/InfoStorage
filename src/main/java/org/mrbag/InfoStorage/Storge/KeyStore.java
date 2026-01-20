package org.mrbag.InfoStorage.Storge;

import org.mrbag.InfoStorage.Util.IdUtils;

import io.micrometer.common.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KeyStore {

	String password;

	String id;

	/**
	 * Определеяет, в системе, являеться ли данная запись хранящийся, или нет
	 * 
	 * @return true - запись еще не сохраннена.
	 */
	public boolean canStore() {
		return id == null || id.isEmpty();
	}

	/**
	 * Проверка коректности данных
	 * 
	 * @return результат проверки коректности данных
	 */
	public boolean isValid() {
		return !password.isBlank();
	}

	/**
	 * Протая реализация устоновки случайного ключа
	 * 
	 * @return результат
	 */
	public KeyStore generateId() {
		id = IdUtils.generateHashUUID();
		return this;
	}

	public static KeyStore generate(@NonNull String password) {
		return new KeyStore(password, IdUtils.generateHashUUID());
	}

}
