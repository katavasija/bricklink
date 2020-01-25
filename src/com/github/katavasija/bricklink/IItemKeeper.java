package com.github.katavasija.bricklink;

public interface IItemKeeper {
	void saveItem(Item item);
	String getItemId(Item item);
	void close();
}
