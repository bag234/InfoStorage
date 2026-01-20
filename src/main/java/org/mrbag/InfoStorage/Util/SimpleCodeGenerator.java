package org.mrbag.InfoStorage.Util;

import java.util.Random;

/**
 * Данный генератор хоть и проблематичен, но достаточно прост что бы закрывть
 * все потребности ситемы
 */
public class SimpleCodeGenerator {

	static Random rand = new Random();

	public static String generateInt() {
		return "" + rand.nextInt(100000, 1000000);
	}

}
