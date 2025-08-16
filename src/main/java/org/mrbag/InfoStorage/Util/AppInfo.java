package org.mrbag.InfoStorage.Util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AppInfo {

	public static String header = "undata"; 
	
	public static String clheader = "undata"; 
	
	@Value("${app.cloud}")
	public void setClHeader(String header) {
		AppInfo.clheader = header;
	}
	
	@Value("${app.header}")
	public void setHeader(String header) {
		AppInfo.header = header;
	}
	
	public static String getCloudHeader() {
		return clheader;
	}
	
	public static String getHeader() {
		return header;
	}
}
