import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;

public class DataGrabber {
	
	private final static String fileExtension = ".html";

	public String getHtmlSource(String url) throws Exception {
		final String startStr = "<table border=1 cellspacing=0 cellpadding=2 width=945>";
		final String endStr = "</table>";
		String htmlSource = new String();
		URL oracle = new URL(url);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				oracle.openStream()));

		String inputLine;
		boolean isStarted = false;
		while ((inputLine = in.readLine()) != null) {
			// Read the Goods table
			if (inputLine.equalsIgnoreCase(startStr) || isStarted) {
				isStarted = true;
				htmlSource += inputLine+"\n";
				if (inputLine.equalsIgnoreCase(endStr)) {
					break;
				}
			}
		}
		in.close();
		

		System.out.println(htmlSource);
		writeToFile(htmlSource);
		return htmlSource;
	}

	private void writeToFile(String stringToWrite) throws FileNotFoundException,
			UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter("the-file-name"+fileExtension, "UTF-8");
		writer.println(stringToWrite);
		writer.close();
	}
}
