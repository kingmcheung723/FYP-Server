public class Main {
	
	private final static String consumerURLZH = "http://www3.consumer.org.hk/pricewatch/supermarket/";
	private final static String consumerURLEN = "http://www3.consumer.org.hk/pricewatch/supermarket/?lang=en";
	
	public static void main(String[] args) throws Exception {
		DataGrabber dg = new DataGrabber();
		dg.getHtmlSource(consumerURLEN);
		
	}
}
