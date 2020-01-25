package com.github.katavasija.bricklink;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class ItemCollector {
	private String itemNo;
	private IItemKeeper itemKeeper;
	private final String WARNING_HEADER = "ItemCollector warning:";
	private final String INFO_HEADER = "ItemCollector info:";
	private final String NO_PROC_ID_WARNING = WARNING_HEADER + " can't collect item id cause of ";
	
	public ItemCollector(String itemNo, IItemKeeper itemKeeper) {
		this.itemNo = itemNo;
		this.itemKeeper = itemKeeper;
	}

	public Item collectSavedItem() {
		System.out.print("collecting item ...");
		Item item = new Item(0, this.itemNo);
		String idString = "";

		if (!StringUtils.IsBlank(this.itemNo) && this.itemKeeper != null) {
			// check id in db
			idString = itemKeeper.getItemId(item);
			if (!StringUtils.IsBlank(idString)) {
				item.appendOperationString(INFO_HEADER + " id collected from db.");
				item.setItemIdFromString(idString);
				item.setItemSaved(true);
 			} else {
 				// request id from bricklink
 				BrickLinkRequest brickLinkRequest = new BrickLinkRequest();
 				String itemIdPage = brickLinkRequest.getItemIdPage(item);
 				if (!StringUtils.IsBlank(itemIdPage)) {
 					// parse response page for id
 					item.appendOperationString(INFO_HEADER + " got itemIdPage response.");
 					idString = parseItemIdPage(itemIdPage);
 					if (!StringUtils.IsBlank(idString)) {
 						item.appendOperationString(INFO_HEADER + " parsed id from itemIdPage.");
 						item.setItemIdFromString(idString);
 						saveItem(item);
 					} else {
 						item.appendOperationString(WARNING_HEADER + " got empty parse result.");
 					}
 					
 				} else {
 					item.appendOperationString(WARNING_HEADER + " got empty itemIdPage response.");
 				}
			}
		}  else {
			// todo logger
			if (StringUtils.IsBlank(this.itemNo)) {
				item.appendOperationString(NO_PROC_ID_WARNING + "empty itemNo.");
			}

			if (itemKeeper == null) {
				item.appendOperationString(NO_PROC_ID_WARNING + "empty itemKeeper");
			}
		}
		System.out.print("Done.");
		return item;
	}

	private String parseItemIdPage(String itemIdPage) {
		String patternString = "idItem:[ \t]*(?<idvalue>[0-9]+)";
		Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);

		Matcher matcher = pattern.matcher(itemIdPage);
		if (matcher.find()) {
			return matcher.group("idvalue");
		} else {
			return "";
		}
	}

	private void saveItem(Item item) {
		if (this.itemKeeper != null) {
			this.itemKeeper.saveItem(item);
		}
		item.setItemSaved(true);
	}

}
