package org.mrbag.InfoStorage.Util;

import java.util.Random;

//Rewrite
public class SimpleCodeGenerator {

	static Random rand = new Random();

	public static String generateInt() {
		return "" + rand.nextInt(10000, 100000);
	}

}
