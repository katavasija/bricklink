package com.github.katavasija.bricklink;

import static java.util.stream.Collectors.toList;
import java.util.Collections;
import java.util.List;

public class StatsMaker {
	
	public static void main(String[] args) {
		ItemsNumListProvider ilp = new ItemsNumListProvider("rawItems.csv");
		List<String> itemsNumList = ilp.getItemsNumList();
		
		List<String> itemsNumListWithoutDups = itemsNumList.stream()
     		.distinct()
     		.collect(toList());

		IItemKeeper itemKeeper = new SqLiteItemKeeper();
		int i = 0;
		for (String itemNum : itemsNumList) {
			ItemCollector itemCollector = new ItemCollector(itemNum, itemKeeper);

			Item item = itemCollector.collectSavedItem();
			
			System.out.println(item.getOperationString());
			System.out.println(itemNum + ';' + item.getItemId());
			StatsCollector statsCollector = new StatsCollector(item);
			statsCollector.collectStats();
			i++;
			if (i > 402) {
				break;
			}
		}
		itemKeeper.close();
	}
}
