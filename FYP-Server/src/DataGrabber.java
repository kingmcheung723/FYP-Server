package src;

import java.io.FileNotFoundException;
import java.io.IOException;
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

	private final static int goodIdIndex = 0, categoryIndex = 1,
			brandIndex = 2, nameIndex = 3;

	public void siteURL(String urlStr) throws IOException {
		URL url = new URL(urlStr);
		// Connection timeout in millisecond
		int timeOut = 30000;
		Document htmlSource = Jsoup.parse(url, timeOut);
		Element table = htmlSource.select("table").get(9);
		this.extractGoodsData(table);
		// this.extractElememts(table, categoryIndex);

	}

	private List<Goods> extractGoodsData(Element table) {
		List<Goods> goods = new ArrayList<>();
		Elements rows = table.select("tr");

		for (int i = 1; i < rows.size(); i++) {
			Element row = rows.get(i);
			Elements cols = row.select("td");

			Goods good = new Goods();
			good.setId(cols.get(goodIdIndex).childNode(0).attributes()
					.get("value"));
			good.setCategory(cols.get(categoryIndex).text());
			good.setBrand(cols.get(brandIndex).text());
			good.setNameZH(cols.get(nameIndex).text());
			good.addPrice(new Price("´f±d", this.priceStringToFloat(cols.get(4)
					.text())));
			good.addPrice(new Price("¦Ê¨Î", this.priceStringToFloat(cols.get(5)
					.text())));
			good.addPrice(new Price("Market Place", this
					.priceStringToFloat(cols.get(6).text())));
			good.addPrice(new Price("¥Ã©ô", this.priceStringToFloat(cols.get(7)
					.text())));
			good.addPrice(new Price("¤j©÷­¹«~", this.priceStringToFloat(cols.get(8)
					.text())));

			System.out.println(good.toString() + "\n");
			goods.add(good);
		}

		return goods;
	}

	private List<String> extractElememts(Element table, int elementIndex) {
		Elements rows = table.select("tr");

		List<String> elements = new ArrayList<>();

		for (int i = 1; i < rows.size(); i++) {
			Element row = rows.get(i);
			Elements cols = row.select("td");

			String categoryName = cols.get(nameIndex).text();
			if (elements.contains(categoryName) == false) {
				System.out.println(categoryName + "\n");
				elements.add(categoryName);
			}
		}
		return elements;
	}

	private float priceStringToFloat(String priceString) {
		priceString = priceString.replaceAll("[^0-9.]", "");
		if (priceString.equalsIgnoreCase("")) {
			priceString = "-1";
		}
		return Float.parseFloat(priceString);
	}

	private void writeToFile(String stringToWrite)
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter("the-file-name" + fileExtension,
				"UTF-8");
		writer.println(stringToWrite);
		writer.close();
	}
} // DataGrabber
