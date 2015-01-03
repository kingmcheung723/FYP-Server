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
	private String id;
	private DiscountPrice discountPrice;
	private String category;
	private String brand;
	private String nameZH;
	private String nameEN;
	private List<Price> prices = new ArrayList<>();
	
	public Goods() {
		super();
	}

	public Goods(String id, DiscountPrice discountPrice, 
			String category, String brand, String nameZH, String nameEN) {
		super();
		
		this.id = id;
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
	 * @param nameZH the nameZH to set
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
	 * @param nameEN the nameEN to set
	 */
	public void setNameEN(String nameEN) {
		this.nameEN = nameEN;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the brand
	 */
	public String getBrand() {
		return brand;
	}

	/**
	 * @param brand the brand to set
	 */
	public void setBrand(String brand) {
		this.brand = brand;
	}

	/**
	 * @param price the price to set
	 */
	public void addPrice(Price price) {
		this.prices.add(price);
	}
	
	/**
	 * @return the discountPrice
	 */
	public DiscountPrice getDiscountPrice() {
		return discountPrice;
	}

	/**
	 * @param discountPrice the discountPrice to set
	 */
	public void setDiscountPrice(DiscountPrice discountPrice) {
		this.discountPrice = discountPrice;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		String priceString = "";
		for (int i = 0; i < this.prices.size(); i++) {
			priceString += this.prices.get(i).toString();
		}
		
		return "Goods [id=" + id + ", nameZH=" + nameZH + ", nameEN=" + nameEN + 
				", price=" + priceString + ", discountPrice="
				+ discountPrice + ", category=" + category + ", brand=" + brand
				+  "]";
	}
}

/**
 * 
 * @author CKM
 * 
 */
class Price {
	
	private String shopName;
	private float price;
	
	public Price(String shopName, float price) {
		super();
		this.shopName = shopName;
		this.price = price;
	}

	/**
	 * @return the shopName
	 */
	public String getShopName() {
		return shopName;
	}

	/**
	 * @param shopName the shopName to set
	 */
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	/**
	 * @return the price
	 */
	public float getPrice() {
		return price;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[shopName=" + shopName + ", price=" + price + "]";
	}

	/**
	 * @param price the price to set
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
	 * @param shopName the shopName to set
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
	 * @param discountPrice the discountPrice to set
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
	 * @param details the details to set
	 */
	public void setDetails(String details) {
		this.details = details;
	}
	
	
}
