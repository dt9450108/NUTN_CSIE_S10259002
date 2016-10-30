import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class BookFetcher {
	private String request;

	public BookFetcher(String request) {
		this.request = request + "?";
	}

	public BookFetcher() {

	}

	public BufferedReader send() {
		try {
			URLConnection connection = new URL(request).openConnection();
			connection.setDoOutput(true);
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			return in;
		} catch (Exception e) {
			System.out.println("BookFetcher.send ERROR");
		}
		return null;
	}

	public void appendfirst(String pro, String val) {
		try {
			request += pro + "=" + URLEncoder.encode(val, "utf-8");
		} catch (Exception e) {
			System.out.println("BookFetcher.appendfirst:String, String ERROR");
		}
	}

	public void appendfirst(String pro, int val) {
		try {
			request += pro + "=" + URLEncoder.encode(String.format("%d", val), "utf-8");
		} catch (Exception e) {
			System.out.println("BookFetcher.appendfirst:String, int ERROR");
		}
	}

	public void append(String pro, String val) {
		try {
			request += "&" + pro + "=" + URLEncoder.encode(val, "utf-8");
		} catch (Exception e) {
			System.out.println("BookFetcher.append:String, int ERROR");
		}
	}

	public void append(String pro, int val) {
		try {
			request += "&" + pro + "=" + URLEncoder.encode(String.format("%d", val), "utf-8");
		} catch (Exception e) {
			System.out.println("BookFetcher.append:String, int ERROR");
		}
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request + "?";
	}
}
