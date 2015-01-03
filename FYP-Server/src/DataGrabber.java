package src;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.*;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DataGrabber {

	private final static String fileExtension = ".html";
	
	private List<Goods> goods = new ArrayList<>();
	
	public void extractHtmlElement(String urlStr) throws IOException {
		URL url = new URL(urlStr);
		// Connection timeout in millisecond
		int timeOut = 30000;
		Document htmlSource = Jsoup.parse(url, timeOut);
		Element table = htmlSource.select("table").get(9); // select the first table.
		Elements rows = table.select("tr");
		String record = "";
		for (int i = 1; i < rows.size(); i++) { // first row is the col names so skip it.
			Element row = rows.get(i);
			Elements cols = row.select("td");
			for (int j = 0; j < cols.size(); j++) {
				System.out.println(cols.get(j).text());
			}
			break;
		}
	}

	private void writeToFile(String stringToWrite)
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter("the-file-name" + fileExtension,
				"UTF-8");
		writer.println(stringToWrite);
		writer.close();
	}
}

