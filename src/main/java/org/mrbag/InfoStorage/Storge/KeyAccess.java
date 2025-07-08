package org.mrbag.InfoStorage.Storge;

import org.mrbag.InfoStorage.Util.AppInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class KeyAccess {

	String password;
	
	String id;
	
	public boolean canStore() {
		return id == null ||id.isEmpty();
	}
	
	public boolean isValid() {
		return !password.isBlank();
	}
	
	public KeyAccess generateId() {
		if (canStore() && isValid()) {
			id = IdUtils.generateHashUUID();
		}
		return this;
	}
	
	@Override
	public String toString() {
		return String.format("%s:%s@%s",AppInfo.getHeader(), id, password);
	}
	
}
