package org.mrbag.InfoStorage.Storge;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
@AllArgsConstructor
public class KeyAccess {

	@NonNull
	String password;
	
	String id;
	
	public boolean canStore() {
		return id.isEmpty();
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
		return String.format("data:%s@%s", id, password);
	}
	
}
