import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;

public class DataGrabber {

	public String getHtmlSource(String url) throws Exception {
		String htmlSource = new String();
		URL oracle = new URL(url);
		BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));

		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			htmlSource += inputLine + "\n";
		}
		in.close();

		System.out.println(htmlSource);
		writeToFile(htmlSource);
		return htmlSource;
	}

	public void writeToFile(String stringToWrite) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter("the-file-name.html", "UTF-8");
		writer.println(stringToWrite);
		writer.close();
	}

}
