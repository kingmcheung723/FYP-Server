import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class DataGrabber {

	public String getHtmlSource(String url) throws Exception {
		String htmlSource = new String();
		URL oracle = new URL(url);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				oracle.openStream()));

		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			htmlSource += inputLine;
		}
		in.close();

		System.out.println(htmlSource);
		return htmlSource;
	}
}
