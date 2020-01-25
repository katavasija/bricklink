package com.github.katavasija.bricklink;

public class Tester {
	public static void main(String[] args) {
		
		
		Item item = new Item(1, "23412354-1");
		Item item2 = new Item(2, "3284732-2");

		IItemWriter itemWriter = new CsvItemWriter(item);
		itemWriter.writeItem();

		itemWriter = new CsvItemWriter(item2);
		itemWriter.writeItem();

		String testPrice = "RUB 1,465.60";
		System.out.println(StringUtils.formatRoublePrice(testPrice));
	}
	
}
