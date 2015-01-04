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

	/** Index of goods value */
	private final static int GoodIdIndex = 0;
	private final static int CategoryIndex = 1;
	private final static int BrandIndex = 2;
	private final static int NameIndex = 3;
	private final static int WellcomeIndex = 4;
	private final static int PARKnSHOPIndex = 5;
	private final static int MarketPlaceIndex = 6;
	private final static int AEONIndex = 7;
	private final static int DCHFoodMartIndex = 8;
	
	/** Index for goods table */
	private final static int GoodsDataTableIndex = 9;

	public void siteURL(String urlStr) throws IOException {
		URL url = new URL(urlStr);
		// Connection timeout in millisecond
		int timeOut = 30000;
		Document htmlSource = Jsoup.parse(url, timeOut);
		Element table = htmlSource.select("table").get(GoodsDataTableIndex);
		this.extractGoodsData(table);
		this.extractElememts(table, CategoryIndex);
	}

	private List<Goods> extractGoodsData(Element table) {
		List<Goods> goods = new ArrayList<>();
		Elements rows = table.select("tr");

		for (int i = 1; i < rows.size(); i++) {
			Element row = rows.get(i);
			Elements cols = row.select("td");

			Goods good = new Goods();
			good.setId(cols.get(GoodIdIndex).childNode(0).attributes()
					.get("value"));
			good.setCategory(cols.get(CategoryIndex).text());
			good.setBrand(cols.get(BrandIndex).text());
			good.setNameZH(cols.get(NameIndex).text());
			good.addPrice(new Price("´f±d", this.priceStringToFloat(cols.get(
					WellcomeIndex).text())));
			good.addPrice(new Price("¦Ê¨Î", this.priceStringToFloat(cols.get(
					PARKnSHOPIndex).text())));
			good.addPrice(new Price("Market Place", this
					.priceStringToFloat(cols.get(MarketPlaceIndex).text())));
			good.addPrice(new Price("¥Ã©ô", this.priceStringToFloat(cols.get(
					AEONIndex).text())));
			good.addPrice(new Price("¤j©÷­¹«~", this.priceStringToFloat(cols.get(
					DCHFoodMartIndex).text())));

			System.out.println(good.toString() + "\n");
			goods.add(good);

			break;
		}

		return goods;
	}

	private List<String> extractElememts(Element table, int elementIndex) {
		Elements rows = table.select("tr");

		List<String> elements = new ArrayList<>();

		for (int i = 1; i < rows.size(); i++) {
			Element row = rows.get(i);
			Elements cols = row.select("td");

			String categoryName = cols.get(NameIndex).text();
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
		PrintWriter writer = new PrintWriter("the-file-name.txt", "UTF-8");
		writer.println(stringToWrite);
		writer.close();
	}
} // DataGrabber
