package src;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	/** Table names */
	public final static String TableCategories = "categories";
	public final static String TableBrands = "brands";
	public final static String TableGoods = "goods";
	public final static String TableShopGoods = "shop_goods";

	/** Shop Id */
	private final static String WellcomeId = "1";
	private final static String PARKnSHOPId = "2";
	private final static String MarketPlaceId = "3";
	private final static String AEONId = "4";
	private final static String DCHFoodMartId = "5";

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

	/** Regex for extract data */
	private final static Pattern regex = Pattern.compile("[a-zA-Z0-9'-]");

	private final static String TokenizerSpitter = " ";

	/** Goods store in memory for calling multiple call */
	List<Goods> goods;

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

		// Extract goods data
		this.extractGoods(tableZH, tableEN);

		// Insert into shop_goods
		this.insertShopGoods(this.extractGoods(tableZH, tableEN));

		// Insert Goods data
		this.insertGoodsData(tableZH, tableEN, isInsertGoods);

		// Insert brands data
		this.insertBrands(tableZH, tableEN, isInsertBrands);

		// Insert categories data
		this.insertCategries(tableZH, tableEN, isInsertCategories);

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
								"SELECT brand_id FROM brands WHERE brands.name_en = ?",
								brandName);

				String categoryName = good.getCategory().getNameEn() != null ? good
						.getCategory().getNameEn() : good.getCategory()
						.getNameZh();

				// Get category id
				int categoryId = JDBCHelper
						.getInstance()
						.getJDBCHelper()
						.queryForInt(
								"SELECT category_id FROM categories WHERE categories.name_en = ?",
								categoryName);

				// Inert into goods JDBCHelper .getInstance()
				int result = JDBCHelper
						.getInstance()
						.getJDBCHelper()
						.execute(
								"INSERT INTO "
										+ TableGoods
										+ " (barcode, name_zh, name_en, brand_id, category_id) VALUES (?, ?, ?, ?, ?)",
								good.getBarcode(), good.getNameZH(),
								good.getNameEN(), brandId, categoryId);
			}
		}
	}

	private void insertShopGoods(List<Goods> goods) {
		for (int i = 0; i < goods.size(); i++) {
			Goods good = goods.get(i);
			int goodId = JDBCHelper
					.getInstance()
					.getJDBCHelper()
					.queryForInt(
							"SELECT good_id FROM goods WHERE goods.barcode = ?",
							good.getBarcode());

			List<ShopPrice> shopPrice = good.getShopPrices();
			for (int j = 0; j < shopPrice.size(); j++) {
				String shopId = shopPrice.get(j).getShopId();
				float price = shopPrice.get(j).getPrice();

				if (goodId > 0) {
//					JDBCHelper
//							.getInstance()
//							.getJDBCHelper()
//							.execute(
//									"INSERT INTO "
//											+ TableShopGoods
//											+ " (shop_id, good_id, price) VALUES (?, ?, ?)",
//									shopId, goodId, price >= 0 ? shopPrice.get(i).getPrice() : null);
				}
				System.out.println(i + " | " + this.shopNameFromShopId(shopId) + " " + String.valueOf(price));
			}
		}
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
			good.setBarcode(cols.get(GoodIdIndex).childNode(0).attributes()
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
			good.addPrice(new ShopPrice(WellcomeId, this
					.priceStringToFloat(cols.get(WellcomeIndex).text())));
			good.addPrice(new ShopPrice(PARKnSHOPId, this
					.priceStringToFloat(cols.get(PARKnSHOPIndex).text())));
			good.addPrice(new ShopPrice(MarketPlaceId, this
					.priceStringToFloat(cols.get(MarketPlaceIndex).text())));
			good.addPrice(new ShopPrice(AEONId, this.priceStringToFloat(cols
					.get(AEONIndex).text())));
			good.addPrice(new ShopPrice(DCHFoodMartId, this
					.priceStringToFloat(cols.get(DCHFoodMartIndex).text())));

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
				String goodsIdString = goods.get(i - 1).getBarcode();
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

		if (shopId == WellcomeId) {
			shopName = Wellcome;
		} else if (shopId == PARKnSHOPId) {
			shopName = PARKnSHOP;
		} else if (shopId == MarketPlaceId) {
			shopName = MarketPlace;
		} else if (shopId == AEONId) {
			shopName = AEON;
		} else if (shopId == DCHFoodMartId) {
			shopName = DCHFoodMart;
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
