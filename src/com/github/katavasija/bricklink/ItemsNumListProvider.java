package com.github.katavasija.bricklink;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;


public class ItemsNumListProvider {
	private String filename;
	
	public ItemsNumListProvider(String filename) {
		this.filename = filename;
	}

	public List<String> getItemsNumList() {
		StringBuilder sb = new StringBuilder();
		List list = new ArrayList<String>(600);

        try (BufferedReader br = Files.newBufferedReader(Paths.get(filename))) {

            String line;
            while ((line = br.readLine()) != null) {
               if (!StringUtils.IsBlank(line)) {
               		/*
               		trim spaces not enough for copied from excel data
               		// list.add(line.trim());
               		*/
               		list.add(line.replaceAll("(\\h*)",""));
               }
            }

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

        return list;
	}
}
