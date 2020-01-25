package com.github.katavasija.bricklink;

public class StringUtils {
	public static boolean IsBlank(String str) {
		if (str == null) return true;
		return (str.trim().isEmpty());
	}

	public static String formatRoublePrice(String price) {
		if (price == null) {
			return null; 
		}
		else {
			return price.replaceAll("RUB ", "").replaceAll("\\,", "").replaceAll("\\.", ",");
		}
	}
}
