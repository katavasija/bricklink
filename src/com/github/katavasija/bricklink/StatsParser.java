package com.github.katavasija.bricklink;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
// import java.io.*;


public class StatsParser {
	
	/*
	public static void main(String[] args) throws Exception {
		StringBuilder sb = new StringBuilder();

		try(
			InputStream in = new FileInputStream("c:\\temp\\tmp.html");
			Reader reader = new InputStreamReader(in);
			BufferedReader buf = new BufferedReader(reader);
			) {
				String line = "";
				while ((line = buf.readLine()) != null) {
					sb.append(line);
				}
			parseStatsTable(sb.toString());
		}
	}*/

	private Item item;
	private String statsPage;
	private static final String WARNING_HEADER = "StatsParser warning:";
	private static final String INFO_HEADER = "StatsParser info:";
	
	public StatsParser(Item item, String statsPage) {
		this.item = item;
		this.statsPage = statsPage;
	}

	public void parseStatsTable() {
			
		String patternString = "(?<valuesTable>pcipgSummaryTable\">(?<tableRow><TR><TD>(?<caption>[\\w\\s-:]+)</TD><TD><b>(?<value>[\\w\\s,\\.]+)</b></TD></TR>)+</table>)";

		Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(this.statsPage);
		
		StringBuilder resultSb = new StringBuilder();
		// yet find the first table
		if (matcher.find()) {
			// resultSb.append(matcher.group("valuesTable"));
			parseStatsRowsIntoIndicatorsValues(matcher.group("valuesTable"));
		} else {
			this.item.appendOperationString(WARNING_HEADER + "no valuesTable found.");
		}
		// return resultSb.toString();
	}

	private void parseStatsRowsIntoIndicatorsValues(String statsTable) {
		
		String rowPatternString = "(?<tableRow><TR><TD>(?<caption>[\\w\\s-:]+)</TD><TD><b>(?<value>[\\w\\s,\\.]+)</b></TD></TR>)";

		Pattern pattern = Pattern.compile(rowPatternString, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(statsTable);

		StatsIndicatorsPress sip = new StatsIndicatorsPress(this.item);
		while (matcher.find()) {
			//System.out.println(matcher.group("caption") + ";" + matcher.group("value"));
			sip.pressIndicatorValue(matcher.group("caption"), matcher.group("value"));
		}
		// System.out.println(item2.toString());
	}
}
