package src;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.TabExpander;

import jdbchelper.JdbcHelper;

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
	
	/** Index for price table */
	private final static int PriceDataTableIndex = 8;
	private final static int PriceDataShopTablesIndex = 1;

	/** Table names */
	public final static String TableCategories = "categories";
	public final static String TableBrands = "brands";
	public final static String TableGoods = "goods";
	public final static String TableShopGoods = "shop_goods";
	public final static String TablePrices = "prices";

	/** Shop Id */
	private final static String WellcomeIdString = "1";
	private final static String PARKnSHOPIdString = "2";
	private final static String MarketPlaceIdString = "3";
	private final static String AEONIdString = "4";
	private final static String DCHFoodMartIdString = "5";
	private final static int WellcomeId = 1;
	private final static int PARKnSHOPId = 2;
	private final static int MarketPlaceId = 3;
	private final static int AEONId = 4;
	private final static int DCHFoodMartId = 5;

	/** Shop Name */
	private final static String Wellcome = "Wellcome";
	private final static String PARKnSHOP = "PARKnSHOP";
	private final static String MarketPlace = "MarketPlace";
	private final static String AEON = "AEON";
	private final static String DCHFoodMart = "DCHFoodMart";

	/** Booleans for indicate isn't extract the follow items */
	private static boolean isInsertBrands = false;
	private static boolean isInsertCategories = false;
	private static boolean isInsertGoods = false;
	private static boolean isInsertPrice = true;

	/* Price web page url */
	private static String ConsumerPriceWebPageChineseURL = "http://www3.consumer.org.hk/pricewatch/supermarket/detail.php?lang=tc&itemcode=";
	private static String ConsumerPriceWebPageEnglishURL = "http://www3.consumer.org.hk/pricewatch/supermarket/detail.php?lang=en&itemcode=";

	/** Regex for extract data */
	private final static Pattern regex = Pattern.compile("[a-zA-Z0-9'-]");

	private final static String TokenizerSpitter = " ";

	/** Goods store in memory for calling multiple call */
	List<Goods> goods;

	public void siteURL(String urlStrZH, String urlStrEN) throws IOException {
		// Connection timeout in millisecond
		final int timeOut = 300000;

		// Get Chinese version HTML source
		URL urlZH = new URL(urlStrZH);
		Document htmlSourceZH = Jsoup.parse(urlZH, timeOut);
		Element tableZH = htmlSourceZH.select("table").get(GoodsDataTableIndex);

		// Get English version HTML source
		URL urlEN = new URL(urlStrEN);
		Document htmlSourceEN = Jsoup.parse(urlEN, timeOut);
		Element tableEN = htmlSourceEN.select("table").get(GoodsDataTableIndex);
		
		// Insert Goods data
//		this.insertGoodsData(tableZH, tableEN, isInsertGoods);

		// Insert brands data
//		this.insertBrands(tableZH, tableEN, isInsertBrands);

		// Insert categories data
//		this.insertCategries(tableZH, tableEN, isInsertCategories);
		
		this.insertPrice(isInsertPrice);

	}

	private void insertCategries(Element tableZH, Element tableEN,
			boolean isEnable) {
		if (isEnable == true) {
			List<String> categoriesListZH = this.extractElememts(tableZH,
					CategoryIndex);
			List<String> categoriesListEN = this.extractElememts(tableEN,
					CategoryIndex);
			for (int i = 0; i < categoriesListZH.size(); i++) {
				String nameZH = categoriesListZH.get(i);
				String nameEN = categoriesListEN.get(i);

				JDBCHelper
						.getInstance()
						.getJDBCHelper()
						.execute(
								"INSERT INTO " + TableCategories
										+ " (name_zh, name_en) VALUES (?, ?)",
								nameZH, nameEN);
			}
		}
	}

	private void insertBrands(Element tableZH, Element tableEN, boolean isEnable) {
		if (isEnable == true) {
			List<String> brandListZH = this
					.extractElememts(tableZH, BrandIndex);
			for (int i = 0; i < brandListZH.size(); i++) {
				StringTokenizer nameToken = new StringTokenizer(
						brandListZH.get(i), TokenizerSpitter);
				String nameZH = "";
				String nameEN = "";
				while (nameToken.hasMoreTokens()) {
					String tokenString = nameToken.nextToken();

					if (this.isChinese(tokenString)) {
						nameZH += nameZH == "" ? tokenString : TokenizerSpitter
								+ tokenString;
					} else {
						nameEN += nameEN == "" ? tokenString : TokenizerSpitter
								+ tokenString;
					}
				}

				JDBCHelper
						.getInstance()
						.getJDBCHelper()
						.execute(
								"INSERT INTO " + TableBrands
										+ " (name_zh, name_en) VALUES (?, ?)",
								nameZH, nameEN);

			}
		}
	}

	private void insertGoodsData(Element tableZH, Element tableEN,
			boolean isEnable) {
		if (isEnable == true) {
			List<Goods> goods = this.extractGoods(tableZH, tableEN);

			for (int i = 0; i < goods.size(); i++) {
				Goods good = goods.get(i);

				// Get name in Chinese if the item is no English name
				String brandName = good.getBrand().getNameEn() != null ? good
						.getBrand().getNameEn() : good.getBrand().getNameZh();

				// Get brand id
				int brandId = JDBCHelper
						.getInstance()
						.getJDBCHelper()
						.queryForInt(
								"SELECT id FROM brands WHERE brands.name_en = ?",
								brandName);

				String categoryName = good.getCategory().getNameEn() != null ? good
						.getCategory().getNameEn() : good.getCategory()
						.getNameZh();

				// Get category id
				int categoryId = JDBCHelper
						.getInstance()
						.getJDBCHelper()
						.queryForInt(
								"SELECT id FROM categories WHERE categories.name_en = ?",
								categoryName);

				// Inert into goods JDBCHelper .getInstance()
				JDBCHelper
						.getInstance()
						.getJDBCHelper()
						.execute(
								"INSERT INTO "
										+ TableGoods
										+ " (consumer_id, name_zh, name_en, brand_id, category_id) VALUES (?, ?, ?, ?, ?)",
								good.getConsumerId(), good.getNameZH(), good.getNameEN(), brandId, categoryId);

			}
		}
	}

	/**
	 * Extract the price for given good
	 * 
	 * @param goood
	 * @throws IOException
	 */
	private void insertPrice(boolean isEnabled) throws IOException {
		if (isEnabled) {
			// Get good's consumer id
			List<String> consumerIds = JDBCHelper.getInstance().getJDBCHelper()
					.queryForStringList("SELECT goods.consumer_id FROM goods");

			if (consumerIds != null && consumerIds.size() > 0) {
				int totalCount = 0;
				for (int i = 0; i < consumerIds.size(); i++) {
					String consumerId = consumerIds.get(i);

					final int timeOut = 3000;

					// Get Chinese version HTML source
					URL urlZH = new URL(ConsumerPriceWebPageChineseURL
							+ consumerId);
					Document htmlSourceZH = Jsoup.parse(urlZH, timeOut);
					Elements tables = htmlSourceZH.select("table");
					if (tables.size() == 0 || tables.size() < PriceDataTableIndex) {
						continue;
					}
					Element tableZH = tables.get(PriceDataTableIndex);

					// Get English version HTML source
					URL urlEN = new URL(ConsumerPriceWebPageEnglishURL
							+ consumerId);
					Document htmlSourceEN = Jsoup.parse(urlEN, timeOut);
					Element tableEN = htmlSourceEN.select("table").get(
							PriceDataTableIndex);

					extractPriceDetails(tableZH, consumerId, false);
					
					totalCount++;
					System.out.println("Index : " + totalCount);
				}
				System.out.println("Total goods : " + totalCount);
			}
		}
	}
	
	private String priceOnlyDigit(String price) {
		// Extract only the digits from the price string
		String priceOnlyDigit = "";
		for (int z = 0; z < price.length(); z++) {

			// Check isn't a chinese
	        int codepoint = price.codePointAt(z);
	        if (Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN) {
	            break;
	        }
			
			char c = price.charAt(z);
			if ((c >= 48 && c <= 57) || (c == '.')) {
				priceOnlyDigit += c;
			}
		}
		return priceOnlyDigit;
	}
	
	private void extractPriceDetails(Element table, String consumerId, Boolean isEn) {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		Date yesterday = cal.getTime();

		Elements trs = table.select("tr");
		// Index 1 is the first table of shops
		Elements shopTables = trs.get(PriceDataShopTablesIndex).select("table");

		int shopId = 0;
		// Extract shop tables
		for (int j = 0; j < shopTables.size(); j++) {
			Elements shopTableTrs = shopTables.get(j).select("tr");
			shopId = j + 1;
			// Extract "tr"s(Price) for shop table
			for (int k = 1; k < shopTableTrs.size(); k++) {
				Elements shopTableTds = shopTableTrs.get(k).select("td");
				String priceDateString = shopTableTds.get(0).text();

				// Only get the current date price
				try {
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"yyyy-MM-dd");
					Date priceDate = dateFormat.parse(priceDateString);

					// If the price date is before today, do not process the
					// price
					int result = priceDate.compareTo(yesterday);
					if (result <= 0) {
						continue;
					}
				} catch (Exception e) {
					continue;
				}
				
				String price = shopTableTds.get(1).text();
				if (price.equalsIgnoreCase("--")) {
					continue;
				} else {
					price = priceOnlyDigit(price);
				}

				// If there is "remark" there is discount price
				String discountDetails = null;
				if (!shopTableTds.select("remark").isEmpty()) {
					Elements remarks = shopTableTds.select("remark");
					discountDetails = remarks.first().text();

				}
				
				int goodId = JDBCHelper
						.getInstance()
						.getJDBCHelper()
						.queryForInt(
								"SELECT id FROM goods WHERE goods.consumer_id = ?",
								consumerId);

				String goodName = JDBCHelper
						.getInstance()
						.getJDBCHelper()
						.queryForString(
								"SELECT name_zh FROM goods WHERE goods.id = ?",
								goodId);

				String shopName = shopNameFromShopId(shopId);

				String sql = null;
				if (isEn) {
					sql = "INSERT INTO "
							+ TableShopGoods
							+ "(shop_id, good_id, price, discount_details_en) VALUES (?, ?, ?, ?)";
				} else {
					sql = "INSERT INTO "
							+ TableShopGoods
							+ "(shop_id, good_id, price, discount_details_zh) VALUES (?, ?, ?, ?)";
				}

				// Inert into goods JDBCHelper .getInstance()
				int result = JDBCHelper.getInstance().getJDBCHelper()
						.execute(sql, shopId, goodId, price, discountDetails);

				System.out.println("Result :" + result + "\n" + "Consumer Id: "
						+ consumerId + "\n" + "Shop Id: " + shopId + "\n"
						+ "Shop: " + shopName + "\n" + "Good: " + goodName
						+ "\n" + "Price: " + price + "\n" + "Discount: "
						+ discountDetails);
			}
		}
	}

	private Elements pricePageTrsForTable(Element table) {
		Elements trs = table.select("tr");
		// Index 1 is the first table of shops
		Elements shopTables = trs.get(PriceDataShopTablesIndex).select("table");
		// Extract shop tables
		for (int j = 0; j < shopTables.size(); j++) {
			Elements shopTableTrs = shopTables.get(j).select("tr");
			// Extract "tr"s for shop table
			for (int k = 1; k < shopTableTrs.size(); k++) {
				Elements shopTableTds = shopTableTrs.get(k).select("td");
				return shopTableTds;
			}
		}
		return null;
	}

	private List<Goods> extractGoods(Element tableZH, Element tableEN) {
		// Extract Chinese goods
		if (this.goods == null) {
			this.goods = new ArrayList<>();
		}

		Elements rows = tableZH.select("tr");
		for (int i = 1; i < rows.size(); i++) {
			Element row = rows.get(i);
			Elements cols = row.select("td");

			Goods good = new Goods();
			good.setConsumerId(cols.get(GoodIdIndex).childNode(0).attributes()
					.get("value"));
			good.getCategory().setNameZh(cols.get(CategoryIndex).text());

			// Extract brand name in Chinese and English
			StringTokenizer nameToken = new StringTokenizer(cols
					.get(BrandIndex).text(), TokenizerSpitter);
			String nameZH = "";
			String nameEN = "";
			while (nameToken.hasMoreTokens()) {
				String tokenString = nameToken.nextToken();
				if (this.isChinese(tokenString)) {
					nameZH += nameZH == "" ? tokenString : TokenizerSpitter
							+ tokenString;
				} else {
					nameEN += nameEN == "" ? tokenString : TokenizerSpitter
							+ tokenString;
				}
			}
			good.getBrand().setNameZh(nameZH);
			good.getBrand().setNameEn(nameEN);

			good.setNameZH(cols.get(NameIndex).text());

			this.goods.add(good);
		}

		// Extract English data
		rows = tableEN.select("tr");
		for (int i = 1; i < rows.size(); i++) {
			Element row = rows.get(i);
			Elements cols = row.select("td");
			// Get good id
			String goodIdString = cols.get(GoodIdIndex).childNode(0)
					.attributes().get("value");

			if (goods.size() >= i) {
				String goodsIdString = goods.get(i - 1).getConsumerId();
				if (goodIdString.equalsIgnoreCase(goodsIdString)) {
					// Set English name into goods
					goods.get(i - 1).setNameEN(cols.get(NameIndex).text());
					// Set English name of category into goods
					goods.get(i - 1).getCategory()
							.setNameEn(cols.get(CategoryIndex).text());
				}
			}
		}

		return this.goods;
	}

	private boolean isChinese(String str) {
		boolean isChinese = false;
		for (int j = 0; j < str.length(); j++) {
			Matcher m = regex.matcher(str.substring(j, j + 1));
			if (m.find() == false) {
				isChinese = true;
				break;
			}
		}
		return isChinese;
	}

	private String shopNameFromShopId(String shopId) {
		String shopName = null;

		if (shopId == WellcomeIdString) {
			shopName = Wellcome;
		} else if (shopId == PARKnSHOPIdString) {
			shopName = PARKnSHOP;
		} else if (shopId == MarketPlaceIdString) {
			shopName = MarketPlace;
		} else if (shopId == AEONIdString) {
			shopName = AEON;
		} else if (shopId == DCHFoodMartIdString) {
			shopName = DCHFoodMart;
		}

		return shopName;
	}

	private String shopNameFromShopId(int shopId) {
		String shopName = null;
		
		switch (shopId) {
		case WellcomeId:
			shopName = Wellcome;
			break;
		case PARKnSHOPId:
			shopName = PARKnSHOP;
			break;
		case MarketPlaceId:
			shopName = MarketPlace;
			break;
		case AEONId:
			shopName = AEON;
			break;
		case DCHFoodMartId:
			shopName = DCHFoodMart;
			break;
		}

		return shopName;
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
