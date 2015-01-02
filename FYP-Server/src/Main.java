import java.io.IOException;


public class Main {

	private final static String consumerURLZH = "http://www3.consumer.org.hk/pricewatch/supermarket/";
	private final static String consumerURLEN = "http://www3.consumer.org.hk/pricewatch/supermarket/?lang=en";
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		DataGrabber dg = new DataGrabber();
//		dg.getHtmlSource(consumerURLEN);
		dg.extractHtmlElement(consumerURLZH);

	}

}
