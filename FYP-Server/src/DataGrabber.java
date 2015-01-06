package src;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;
import javax.xml.transform.Templates;

import jdbchelper.SimpleDataSource;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.omg.CosNaming.NameHelper;

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

	/** Table names */
	public final static String TableCategories = "categories";
	public final static String TableBrands = "brands";

	/** Index for goods table */
	private final static int GoodsDataTableIndex = 9;

	public void siteURL(String urlStrZH, String urlStrEN) throws IOException {
		// Connection timeout in millisecond
		final int timeOut = 30000;

		// Get Chinese version HTML source
		URL urlZH = new URL(urlStrZH);
		Document htmlSourceZH = Jsoup.parse(urlZH, timeOut);
		Element tableZH = htmlSourceZH.select("table").get(GoodsDataTableIndex);

		// Get English version HTML source

		URL urlEN = new URL(urlStrEN);
		Document htmlSourceEN = Jsoup.parse(urlEN, timeOut);
		Element tableEN = htmlSourceEN.select("table").get(GoodsDataTableIndex);

		// Extract Goods data
		this.extractGoodsData(tableZH);
		this.extractGoodsData(tableEN);

		// Extract categories
		List<String> categoriesListZH = this.extractElememts(tableZH,
				CategoryIndex);
		List<String> categoriesListEN = this.extractElememts(tableEN,
				CategoryIndex);
		for (int i = 0; i < categoriesListZH.size()
				|| i < categoriesListEN.size(); i++) {
			String nameZH = categoriesListZH.get(i);
			String nameEN = categoriesListEN.get(i);

			// JDBCHelper.getInstance().getJDBCHelper().execute("INSERT INTO " +
			// TableCategories +" (name_zh, name_en) VALUES (?, ?)", nameZH,
			// nameEN);
		}

		// Extract brands
		List<String> brandListZH = this.extractElememts(tableZH, BrandIndex);
		List<String> brandListEN = this.extractElememts(tableEN, BrandIndex);
		Pattern regex = Pattern.compile("[a-zA-Z0-9]");
		for (int i = 0; i < brandListZH.size(); i++) {
			// String nameZH = brandListZH.get(i);
			// String nameEN = brandListEN.get(i);
			StringTokenizer nameToken = new StringTokenizer(brandListZH.get(i), " ");
			String nameZH = "";
			String nameEN = "";
			while (nameToken.hasMoreTokens()) {
				String tempString = nameToken.nextToken();
			      Matcher m = regex.matcher(tempString.substring(0, 1));
				if (m.find() == false) {
					nameZH += tempString;
				} else {
					nameEN += tempString;
				}
			}	

			 JDBCHelper.getInstance().getJDBCHelper().execute("INSERT INTO " +  TableBrands +" (name_zh, name_en) VALUES (?, ?)", nameZH, nameEN);
		}
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

			// System.out.println(good.toString() + "\n");
			goods.add(good);

			break;
		}

		return goods;
	}

	private List<String> extractElememts(Element table, final int elementIndex) {
		Elements rows = table.select("tr");

		List<String> elements = new ArrayList<>();

		for (int i = 1; i < rows.size(); i++) {
			Element row = rows.get(i);
			Elements cols = row.select("td");
			if (elements.contains(cols.get(elementIndex).text()) == false) {
				elements.add(cols.get(elementIndex).text());
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
