package com.github.katavasija.bricklink;

public class StatsIndicatorsPress {
	private final String WARNING_HEADER = "StatsIndicatorsPress warning:";
	private final String INFO_HEADER = "StatsIndicatorsPress info:";

	private Item item;
	/*
	timesSold;
	totalQty;
	minPrice;
	avgPrice;
	qtyAvgPrice;
	maxPrice;
	*/
	
	public StatsIndicatorsPress(Item item) {
		this.item = item;
	}

	public void pressIndicatorValue(String indicatorCode, String value) {
		// System.out.println(indicatorCode);
		// System.out.println(value);

		if (StringUtils.IsBlank(indicatorCode)) {
			this.item.appendOperationString(WARNING_HEADER + "indicatorCode is blank, skipping");
			return;
		}

		if (StringUtils.IsBlank(value)) {
			this.item.appendOperationString(INFO_HEADER + "blank value for indicator with code:" + indicatorCode);
		}

		ItemStatsIndicators itemStats =  this.item.getItemStats();
		
		String timesSoldPattern = "(?i:times\\ssold.*)";
		if (indicatorCode.matches(timesSoldPattern)) {
			itemStats.setTimesSold(value);
			return;
		}

		String totalQtyPattern = "(?i:total\\sqty.*)";
		if (indicatorCode.matches(totalQtyPattern)) {
			itemStats.setTotalQty(value);
			return;
		}

		String minPricePattern = "(?i:min\\sprice.*)";
		if (indicatorCode.matches(minPricePattern)) {
			itemStats.setMinPrice(StringUtils.formatRoublePrice(value));
			return;
		}

		String avgPricePattern = "(?i:avg\\sprice.*)";
		if (indicatorCode.matches(avgPricePattern)) {
			itemStats.setAvgPrice(StringUtils.formatRoublePrice(value));
			return;
		}

		String qtyAvgPricePattern = "(?i:qty\\savg\\sprice.*)";
		if (indicatorCode.matches(qtyAvgPricePattern)) {
			itemStats.setQtyAvgPrice(StringUtils.formatRoublePrice(value));
			return;
		}

		String maxPricePattern = "(?i:max\\sprice.*)";
		if (indicatorCode.matches(maxPricePattern)) {
			itemStats.setMaxPrice(StringUtils.formatRoublePrice(value));
			return;
		}

	}

}
