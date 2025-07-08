package org.mrbag.InfoStorage.Util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AppInfo {

	public static String header = "undata"; 
	
	@Value("${app.header}")
	public void setHeader(String header) {
		AppInfo.header = header;
	}
	
	public static String getHeader() {
		return header;
	}
}
