/**
 * 
 */
package src;

/**
 * @author CKM
 * 
 */
public class Goods {
	
	/** Private valiables for the properties of Goods */
	private String id;
	private Price price;
	private DiscountPrice discountPrice;
	private String category;
	private String brand;
	private String name;

	public Goods(String id, Price price, DiscountPrice discountPrice, 
			String category, String brand, String name) {
		super();
		
		this.id = id;
		this.price = price;
		this.discountPrice = discountPrice;
		this.category = category;
		this.brand = brand;
		this.name = name;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the price
	 */
	public Price getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(Price price) {
		this.price = price;
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
