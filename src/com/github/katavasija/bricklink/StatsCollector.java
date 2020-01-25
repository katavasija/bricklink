package com.github.katavasija.bricklink;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class StatsCollector {
	private Item item;
	private final String WARNING_HEADER = "StatsCollector warning:";
	private final String INFO_HEADER = "StatsCollector info:";
	
	public StatsCollector(Item item) {
		this.item = item;
	}

	public void collectStats() {
		System.out.print("collecting stats ...");

		if (this.item.getItemId() > 0) {
			BrickLinkRequest brickLinkRequest = new BrickLinkRequest();
			String statsPage = brickLinkRequest.getItemStatsPage(this.item);
			StatsParser statsParser = new StatsParser(this.item, statsPage);
			statsParser.parseStatsTable();
			IItemWriter itemWriter = new CsvItemWriter(item);
			itemWriter.writeItem();
		}  else {
			// todo logger
			item.appendOperationString(WARNING_HEADER + "refusal to collect stats cause of empty item id.");
		}
		
		// System.out.print("Done.");
	}

}
