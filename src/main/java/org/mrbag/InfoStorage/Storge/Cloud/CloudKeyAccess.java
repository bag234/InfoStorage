package org.mrbag.InfoStorage.Storge.Cloud;

import java.time.LocalDateTime;

import org.mrbag.InfoStorage.Util.AppInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Data
@Getter
@Setter
@AllArgsConstructor
public class CloudKeyAccess {

	String key;
	
	@JsonIgnore
	@Builder.Default
	String password = "";
	
	@Builder.Default
	boolean isSingle = false; 
	
	@Builder.Default
	TypeAccessPassword type = TypeAccessPassword.NONE;
	
	@Builder.Default
	LocalDateTime time = LocalDateTime.now();
	
	public String toStringAccessKey() {
		return String.format("%s:%s@%s://%s/%s/valid", 
				AppInfo.getCloudHeader(), key, password, type, time.toString());
	}
	//XXX check coreect using
	public String keyAlias() {
		return "alias:" + key;
	}
	
	public String contentAlias() {
		return String.format("%s;:%s;:%s;:%s;:%s", 
				key, password, isSingle ? "yes" : "no", type, time);
	}
	
	public static CloudKeyAccess parse(String mess) {
		String[] sp = mess.split(";:");
		if (sp.length < 4) return null; 
		return CloudKeyAccess.builder()
				.key(sp[0])
				.password(sp[1])
				.isSingle(sp[2].equals("yes"))
				.type(TypeAccessPassword.valueOf(sp[3]))
				.time(LocalDateTime.parse(sp[4]))
				.build();
	}
	
}
