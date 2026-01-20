package org.mrbag.InfoStorage.Storge;

import org.mrbag.InfoStorage.Util.AppInfo;

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

	public boolean canStore() {
		return id == null || id.isEmpty();
	}

	public boolean isValid() {
		return !password.isBlank();
	}

	public KeyStore generateId() {
		if (canStore() && isValid()) {
			id = IdUtils.generateHashUUID();
		}
		return this;
	}

	@Override
	public String toString() {
		return String.format("%s:%s@%s", AppInfo.getHeader(), id, password);
	}

}
