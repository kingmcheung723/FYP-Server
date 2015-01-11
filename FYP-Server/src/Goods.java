/**
 * 
 */
package src;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CKM
 * 
 */
public class Goods {

	/** Private valiables for the properties of Goods */
	private String barcode;
	private DiscountPrice discountPrice;
	private Categories category;
	private Brands brand;
	private String nameZH;
	private String nameEN;
	private List<ShopPrice> shopPrices = new ArrayList<>();

	public Goods() {
		super();
	}

	public Goods(String barcode, DiscountPrice discountPrice,
			Categories category, Brands brand, String nameZH, String nameEN) {

		super();

		this.barcode = barcode;
		this.discountPrice = discountPrice;
		this.category = category;
		this.brand = brand;
		this.nameZH = nameZH;
		this.nameZH = nameEN;
	}

	/**
	 * @return the nameZH
	 */
	public String getNameZH() {
		return nameZH;
	}

	/**
	 * @param nameZH
	 *            the nameZH to set
	 */
	public void setNameZH(String nameZH) {
		this.nameZH = nameZH;
	}

	/**
	 * @return the nameEN
	 */
	public String getNameEN() {
		return nameEN;
	}

	/**
	 * @param nameEN
	 *            the nameEN to set
	 */
	public void setNameEN(String nameEN) {
		this.nameEN = nameEN;
	}

	/**
	 * @return the id
	 */
	public String getBarcode() {
		return barcode;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	/**
	 * @return the brand
	 */
	public Brands getBrand() {
		if (this.brand == null) {
			this.brand = new Brands();
		}
		return brand;
	}

	/**
	 * @param shopPrice
	 *            the price to set
	 */
	public void addPrice(ShopPrice shopPrice) {
		this.shopPrices.add(shopPrice);
	}

	/**
	 * @return the shopPrices
	 */
	public List<ShopPrice> getShopPrices() {
		return shopPrices;
	}

	/**
	 * @return the discountPrice
	 */
	public DiscountPrice getDiscountPrice() {
		return discountPrice;
	}

	/**
	 * @param discountPrice
	 *            the discountPrice to set
	 */
	public void setDiscountPrice(DiscountPrice discountPrice) {
		this.discountPrice = discountPrice;
	}

	/**
	 * @return the category
	 */
	public Categories getCategory() {
		if (this.category == null) {
			this.category = new Categories("", "");
		}
		return category;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		String priceString = "";
		for (int i = 0; i < this.shopPrices.size(); i++) {
			priceString += this.shopPrices.get(i).toString();
		}

		return "Goods [id=" + barcode + ", nameZH=" + nameZH + ", nameEN="
				+ nameEN + ", price=" + priceString + ", discountPrice="
				+ discountPrice + ", category=" + category.getNameZh() + ", "
				+ category.getNameEn() + ", brand=" + brand.getNameZh() + ", "
				+ brand.getNameEn() + "]";
	}
}

/**
 * 
 * @author CKM
 * 
 */
class ShopPrice {

	private String shopId;
	private float price;

	public ShopPrice(String shopName, float price) {
		super();
		this.shopId = shopName;
		this.price = price;
	}

	/**
	 * @return the shopName
	 */
	public String getShopId() {
		return shopId;
	}

	/**
	 * @param shopName
	 *            the shopName to set
	 */
	public void setShopName(String shopName) {
		this.shopId = shopName;
	}

	/**
	 * @return the price
	 */
	public float getPrice() {
		return price;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[shopId=" + shopId + ", price=" + price + "]";
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(float price) {
		this.price = price;
	}
}

class DiscountPrice {

	private String shopName;
	private float discountPrice;
	private String details;

	/**
	 * @param shopName
	 * @param discountPrice
	 * @param details
	 */
	public DiscountPrice(String shopName, float discountPrice, String details) {
		super();
		this.shopName = shopName;
		this.discountPrice = discountPrice;
		this.details = details;
	}

	/**
	 * @return the shopName
	 */
	public String getShopName() {
		return shopName;
	}

	/**
	 * @param shopName
	 *            the shopName to set
	 */
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	/**
	 * @return the discountPrice
	 */
	public float getDiscountPrice() {
		return discountPrice;
	}

	/**
	 * @param discountPrice
	 *            the discountPrice to set
	 */
	public void setDiscountPrice(float discountPrice) {
		this.discountPrice = discountPrice;
	}

	/**
	 * @return the details
	 */
	public String getDetails() {
		return details;
	}

	/**
	 * @param details
	 *            the details to set
	 */
	public void setDetails(String details) {
		this.details = details;
	}

}

class Brands {

	private String nameZh;
	private String nameEn;

	/**
	 * @param nameZh
	 * @param nameEn
	 */
	public Brands(String nameZh, String nameEn) {
		super();
		this.nameZh = nameZh;
		this.nameEn = nameEn;
	}

	/**
	 * 
	 */
	public Brands() {
		super();
	}

	/**
	 * @return the nameZh
	 */
	public String getNameZh() {
		return nameZh;
	}

	/**
	 * @param nameZh
	 *            the nameZh to set
	 */
	public void setNameZh(String nameZh) {
		this.nameZh = nameZh;
	}

	/**
	 * @return the nameEn
	 */
	public String getNameEn() {
		return nameEn;
	}

	/**
	 * @param nameEn
	 *            the nameEn to set
	 */
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

}

class Categories {

	private String nameZh;
	private String nameEn;

	public Categories() {
		super();
	}

	/**
	 * @param nameZh
	 * @param nameEn
	 */
	public Categories(String nameZh, String nameEn) {
		super();
		this.nameZh = nameZh;
		this.nameEn = nameEn;
	}

	/**
	 * @return the nameZh
	 */
	public String getNameZh() {
		return nameZh;
	}

	/**
	 * @param nameZh
	 *            the nameZh to set
	 */
	public void setNameZh(String nameZh) {
		this.nameZh = nameZh;
	}

	/**
	 * @return the nameEn
	 */
	public String getNameEn() {
		return nameEn;
	}

	/**
	 * @param nameEn
	 *            the nameEn to set
	 */
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

}
