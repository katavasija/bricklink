package com.github.katavasija.bricklink;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;
import java.net.*;
import javax.net.ssl.HttpsURLConnection;
import java.util.zip.GZIPInputStream;

public class BrickLinkRequest {
	private static String urlString;
	private static String WARNING_HEADER = "request warning:";
	private static String INFO_HEADER = "request info:";
	private static String MESSAGE_HEADER = "";
	private static int MAX_MS_CON_TIMEOUT = 5000;
	private static int ID_MS_PAUSE = 5000;
	private static int PAGE_MS_PAUSE = 10000;
	private static boolean dumpResponse = false;
	private static boolean lockFlag = false;

	// suppose response fits a String
	public static String getItemIdPage(Item item) {
		MESSAGE_HEADER = "GET itemIdPage ";
		urlString = "https://www.bricklink.com/v2/catalog/catalogitem.page?S=" + item.getItemNo() + "#T=P";

		return getResponse(item, false, ID_MS_PAUSE);
	}

	public static String getItemStatsPage(Item item) {
		MESSAGE_HEADER = "GET itemStatsPage ";
		urlString = "https://www.bricklink.com/v2/catalog/catalogitem_pgtab.page?idItem=" + item.getItemId() + "&st=2&gm=1&gc=0&ei=0&prec=2&showflag=0&showbulk=0&currency=117";

		return getResponse(item, true, PAGE_MS_PAUSE);
	}
	

	private static String getResponse(Item item, boolean dumpContent, int msPause) {
		if (lockFlag) {
			return emptyResponseWithInfo(item, "no response because of request lock.");
		} else {
			System.out.println(urlString);
			String response = getResponseString(item, dumpContent);
			pause(msPause);
			return response;
		}
	}

	private static String getResponseString(Item item, boolean dumpContent) {
		URL url = null;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException ex) {
			return emptyResponseWithWarning(item, "MalformedURLException occured.");
		}
		HttpsURLConnection con = null;
		
		try {
			con = (HttpsURLConnection) url.openConnection();
			setConnectionProperties(item, con);
		}
		catch (IOException ex) {
			return emptyResponseWithWarning(item, "failed to connect.");
		}
		int responseCode = 0;
		String encoding = "";
		String responseString = "";

		if (con != null) {
			try {
				responseCode = con.getResponseCode();
				encoding = con.getContentEncoding();
			} catch (IOException ex) {
				// todo logger ex.printStackTrace();
				responseString = emptyResponseWithWarning(item, "failed to get response");
			}

			System.out.println("responseCode:" + responseCode);
			System.out.println("responseEncoding:" + encoding);

			if (responseCode >= 200 && responseCode < 300) {
				try {
					if (encoding.equals("gzip")) {
						responseString = getZippedRequestContent(con, dumpContent);
					} else {
						responseString = getRequestContent(con, dumpContent);
					}
				} catch (Exception ex) {
					responseString = emptyResponseWithWarning(item, "failed to get response content");
				}
			} else {
				boolean needRedirect = (responseCode == HttpURLConnection.HTTP_MOVED_TEMP
										|| responseCode == HttpURLConnection.HTTP_MOVED_PERM
										|| responseCode == HttpURLConnection.HTTP_SEE_OTHER);
				if (needRedirect) {
					String newUrl = con.getHeaderField("Location");
					responseString = emptyResponseWithInfo(item, "Redirect to URL : " + newUrl);
				}
			}
			con.disconnect();
			return responseString;
		} else {
			return emptyResponseWithWarning(item, "failed to connect.");
		}
	}


	private static void setConnectionGetMethod(Item item, HttpsURLConnection con) {
			try {
				con.setRequestMethod("GET");
			}
			catch (ProtocolException ex) {
				emptyResponseWithWarning(item, "failed setting method of connection");
			}
	}

	private static void setCookies(HttpsURLConnection con) {
		 // Set the cookie value to send
		StringBuilder cookieBuilder = new StringBuilder();
		cookieBuilder.append("blLastCatalogTab=P; cartBuyerID=-573554188; blckMID=16cb843542200000-aed9acb5c075b425;");
		cookieBuilder.append("_ga=GA1.2.605505328.1566459517; _gid=GA1.2.1458605896.1566459517; viewCurrencyID=117;");
		cookieBuilder.append("BLNEWSESSIONID=AFF07F687822D0DAF26D06CE7C95EE2A; blckBannerView=[1059]; blckSessionStarted=1");

		con.setRequestProperty("Cookie", cookieBuilder.toString());
	}
	private static void setConnectionProperties(Item item, HttpsURLConnection con) {
		if (con != null) {
			setConnectionGetMethod(item, con);
			con.setConnectTimeout(MAX_MS_CON_TIMEOUT);
			con.setReadTimeout(MAX_MS_CON_TIMEOUT);
			
			con.setRequestProperty("Connection", "keep-alive");
			con.setRequestProperty("Host", "www.bricklink.com");
			con.setRequestProperty("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7");
			con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
			con.setRequestProperty("Cache-Control", "max-age=0");
			//con.setRequestProperty("Accept-Encoding", "utf-8");
			con.setRequestProperty("Accept-Encoding", "gzip, utf-8");
			con.setRequestProperty("Upgrade-Insecure-Requests", "1");
			setCookies(con);
		}
	}

	private static void pause(int msPause) {
		lockFlag = true;
		try {
			Thread.sleep(msPause);
		}
		catch (InterruptedException ex) {
			System.err.format("InterruptedException: %s%n", ex);
		}
		lockFlag = false;
	}

	private static String getRequestContent(HttpsURLConnection con, boolean dumpContent) throws Exception {
		
		try ( 
				Reader reader =  new InputStreamReader(con.getInputStream(), "utf-8");
				BufferedReader buf = new BufferedReader(reader);
				BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\temp\\tmp.html"));
			) 
			{
				String line = "";
				StringBuilder sb = new StringBuilder();
				while ((line = buf.readLine()) != null) {
					sb.append(line);
					if (dumpContent) {
						writer.write(line);
					}
				}
				writer.close();
				return sb.toString();
			}
	}

	private static String getZippedRequestContent(HttpsURLConnection con, boolean dumpContent) throws Exception {
		try ( 
				Reader reader = new InputStreamReader(new GZIPInputStream(con.getInputStream()));
				BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\temp\\tmp.html"));
			)
			{
				StringBuilder sb = new StringBuilder();
				while (true) {
         			int ch = reader.read();
         			if (ch==-1) {
            			break;
         			}
         			sb.append((char)ch);
      			}
      			writer.write(sb.toString());
      			writer.close();
      			return sb.toString();
			}
	}

	private static String emptyResponseWithWarning(Item item, String warning) {
		item.appendOperationString(MESSAGE_HEADER + WARNING_HEADER + warning);
		return "";
	}

	private static String emptyResponseWithInfo(Item item, String info) {
		item.appendOperationString(MESSAGE_HEADER + INFO_HEADER + info);
		return "";
	}
}
