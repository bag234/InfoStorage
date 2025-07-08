package org.mrbag.InfoStorage.Storge;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public final class IdUtils {

	static MessageDigest md; 
	static {
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			while(true) {
				System.err.println("fatal algoritm");
			}
		}
	}
	
	
	public static String generateSimpleId() {
		try {
			return Long.toString(ThreadLocalRandom.current().nextLong(), 36).substring(10);
		} catch (StringIndexOutOfBoundsException e) {
			return generateSimpleId();
		}
	}
	
	public static String generateHashUUID() {
		return new BigInteger(1, md.digest(UUID.randomUUID().toString().getBytes())).toString(36).substring(0, 10);
	}
	
	public static void main(String[] args) {
		HashSet<String> set = new HashSet<String>();
		int col = 0;
		for (int i = 0; i < 1000000; i++) {
			String some = generateHashUUID();
			if (set.contains(some))
				col++;
			set.add(some);
		}
		System.out.println(col);
	}
	
}
