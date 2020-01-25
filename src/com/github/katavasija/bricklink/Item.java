package com.github.katavasija.bricklink;

public class Item {
	private String itemNo;
	private int itemId;
	private boolean itemSaved;
	private ItemStatsIndicators itemStats;
	private StringBuilder operationStringBuilder;
	private StringBuilder toStringBuilder;

	public Item(int itemId, String itemNo) {
		this.itemNo = itemNo;
		this.itemId = itemId;
		this.itemStats = new ItemStatsIndicators();
		this.operationStringBuilder = new StringBuilder();
		this.toStringBuilder = new StringBuilder();
	}

	public String getItemNo() {
		return this.itemNo;
	}

	public int getItemId() {
		return this.itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public void setItemIdFromString(String itemId) {
		this.itemId = Integer.parseInt(itemId);
	}

	public boolean getItemSaved() {
		return this.itemSaved;
	}

	public void setItemSaved(boolean value) {
		this.itemSaved = value;
	}

	public String getOperationString() {
		return this.operationStringBuilder.toString();
	}

	public void setOperationString(String value) {
		this.operationStringBuilder.setLength(0);
		appendOperationString(value);
	}

	public void appendOperationString(String value) {
		// todo StringBuilder
		this.operationStringBuilder.append(value);
	}

	public ItemStatsIndicators getItemStats() {
		return this.itemStats;
	}

	public void setItemStats(ItemStatsIndicators itemStats) {
		this.itemStats = itemStats;
	}

	@Override
	public String toString() {
		this.toStringBuilder.setLength(0);
		
		if (!StringUtils.IsBlank(this.itemNo)) {
			this.toStringBuilder.append("itemNo:" + this.itemNo + ";");
		}
		if (!StringUtils.IsBlank(Integer.toString(this.itemId))) {
			this.toStringBuilder.append("itemId:" + this.itemId + ";");
		}

		if (this.itemStats != null) {
			if (!StringUtils.IsBlank(this.itemStats.getTimesSold())) {
				this.toStringBuilder.append("timesSold:" + this.itemStats.getTimesSold() + ";");
			}
			if (!StringUtils.IsBlank(this.itemStats.getTotalQty())) {
				this.toStringBuilder.append("totalQty:" + this.itemStats.getTotalQty() + ";");
			}
			if (!StringUtils.IsBlank(this.itemStats.getMinPrice())) {
				this.toStringBuilder.append("minPrice:" + this.itemStats.getMinPrice() + ";");
			}
			if (!StringUtils.IsBlank(this.itemStats.getMaxPrice())) {
				this.toStringBuilder.append("maxPrice:" + this.itemStats.getMaxPrice() + ";");
			}
			if (!StringUtils.IsBlank(this.itemStats.getAvgPrice())) {
				this.toStringBuilder.append("avgPrice:" + this.itemStats.getAvgPrice() + ";");
			}
			if (!StringUtils.IsBlank(this.itemStats.getQtyAvgPrice())) {
				this.toStringBuilder.append("qtyAvgPrice:" + this.itemStats.getQtyAvgPrice() + ";");
			}
		}

		return this.toStringBuilder.toString();
	}
}
