package com.github.katavasija.bricklink;
import java.io.*;


public class CsvItemWriter implements IItemWriter {
	/*
		https://www.baeldung.com/java-csv
	*/

	private final String WARNING_HEADER = "CsvItemStatsWriter warning:";
	private final String CSV_FILE_NAME = "itemsOut.csv";
	private Item item;

	public CsvItemWriter(Item item) {
		this.item = item;
	}

	@Override
	public void writeItem() {
		try (FileWriter fileWriter = new FileWriter(CSV_FILE_NAME, true); PrintWriter pw = new PrintWriter(fileWriter)) {
			pw.println(getItemStringLine());
			pw.close();
    	} catch (IOException ex) {
    		this.item.appendOperationString(WARNING_HEADER + "error writing item '" + this.item.getItemNo() + "'");
    	}
	}

	private String getItemStringLine() {
		ItemStatsIndicators itemStats =  this.item.getItemStats();

		return String.join(";", 
						   this.item.getItemNo(),
						   itemStats.getTimesSold(),
						   itemStats.getTotalQty(),
						   itemStats.getMinPrice(),
						   itemStats.getMaxPrice(),
						   itemStats.getAvgPrice(),
						   itemStats.getQtyAvgPrice()
						   );
	}

	private String escapeSpecialCharacters(String data) {
    	String escapedData = data.replaceAll("\\R", " ");
    	if (data.contains(",") || data.contains("\"") || data.contains("'")) {
        	data = data.replace("\"", "\"\"");
        	escapedData = "\"" + data + "\"";
    	}
    	return escapedData;
	}
}
