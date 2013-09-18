package it.unibo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class Network {

	/**
	 * Gets the InputStream of the given URL
	 * 
	 * @param url String that represents an URL
	 * @return InputStream
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static InputStream getInputStream(String url) throws IllegalStateException, IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpGet httpGet = new HttpGet(url.replaceAll(" ", "%20"));
		HttpResponse response = httpClient.execute(httpGet, localContext);
		HttpEntity entity = response.getEntity();
		return entity.getContent();
	}

	/**
	 * Gets the InputStream of the given URL
	 * 
	 * @param url URL
	 * @return InputStream
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static InputStream getInputStream(URL url) throws IllegalStateException, IOException {
		return getInputStream(url.toString());
	}

	/**
	 * Encode the passed URL to UTF8
	 * 
	 * @return
	 */
	public static String encodeURL(String url) {
		int index = url.lastIndexOf('/');
		String firstToken = url.substring(0, index + 1);
		String secondToken = "";
		try {
			secondToken = URLEncoder.encode(url.substring(index + 1), "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			throw new IllegalStateException("Impossible to encode URL",ex);
		}
		return firstToken + secondToken;
	}

	/**
	 * checks a given URL for availbility
	 * 
	 * @param urlN given URL
	 * @return true if URL exists otherwise false
	 */
	public static boolean urlExist(URL urlN) {
		try {
			HttpURLConnection.setFollowRedirects(false);
			HttpURLConnection con = (HttpURLConnection) urlN.openConnection();
			con.setRequestMethod("HEAD");
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Returns the raw data sent from the webservice.
	 * 
	 * @param url The url that contains the request
	 * @return A string that contains the webservice response
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public static String getRawData(String url) throws IllegalStateException, IOException {
		InputStream is = getInputStream(url);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line, response = "";

		while ((line = br.readLine()) != null) {
			response += line + "\n";
		}
		return response;
	}

}
