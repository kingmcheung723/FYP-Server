
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
	
	private List<String> goods = new ArrayList<>();

	public String getHtmlSource(String url) throws Exception {
		final String startStr = "<table border=1 cellspacing=0 cellpadding=2 width=945>";
		final String endStr = "</table>";
		final String startRowTag = "<tr";
		final String engRowTag = "</tr>";
		final String startColTag = "<td";
		final String engColTag = "</td>";

		String htmlSource = new String();
		URL oracle = new URL(url);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				oracle.openStream()));

		String inputLine;
		boolean isStarted = false;
		while ((inputLine = in.readLine()) != null) {
			// Read the Goods table
			if (inputLine.equalsIgnoreCase(startStr) || isStarted) {
				isStarted = true;
				htmlSource += inputLine + "\n";

				// If the line reach </table> means the table ended
				if (inputLine.equalsIgnoreCase(endStr)) {
					// Break the loop
					break;
				}
			}
		}
		in.close();

		System.out.println(htmlSource);
		writeToFile(htmlSource);
		return htmlSource;
	}

	public void extractHtmlElement(String urlStr) throws IOException {
		URL url = new URL(urlStr);
		// Connection timeout in millisecond
		int timeOut = 30000;
		Document doc = Jsoup.parse(url, timeOut);
		Element table = doc.select("table").get(9); // select the first table.
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
