package com.spms.news;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.spms.Util;
import com.spms.ticker.tools.Requests;

public class ImageFromURL {

	private static List<String> apiKeys;
	private static final Integer timeout = 20000;
	private static final Logger log = LogManager.getLogger(ImageFromURL.class);
	
	static {
		apiKeys = new ArrayList<String>();
		apiKeys.add("5cc0d57aed6260be7a091252e36e2e42838b9ac58ca38");
		apiKeys.add("5cc0d5cec0d392e374c73d4d7b6d776b0047cf309c96a");
		apiKeys.add("5cc0d62a34f6dfe065cf78f3f3c12b6af5a4f1ee790fb");
		apiKeys.add("5cc12c79514a1cbbc61016413025437ee648bc91bee19");
		apiKeys.add("5cc12c925814caa2bb9596f12d0f4fa0271b0d80dbf18");
		apiKeys.add("5cc12ca097281dc38c86788d92f88c2d11fe217bc57eb");
		apiKeys.add("5cc12caeb138a8664ff5bf65650d02f11a422f1010b5c");
		apiKeys.add("5cc12cc21b88cb3194b0d044dc54d9c9f5188d14aebae");
		apiKeys.add("5cc12cc21b88cb3194b0d044dc54d9c9f5188d14aebae");
		apiKeys.add("5cc12ce0b6272bb258a61b2b578681ca9bb5bcf8d4daf");
		apiKeys.add("5cc12cf160245b1a6da1fe555b20268bf9ce70ed980e7");
		apiKeys.add("5cc12d009d036c36214df5bdf67eaf55e6620f468b0ec");
		apiKeys.add("5cc12d1051d937c04ffdf63fedddcb42474caf8c06540");
		apiKeys.add("5cc12d206b15a899637611106fa77ee41097ef65f60af");
		apiKeys.add("5cc12d2e50da2a42960f37b1d008a7ef84a3d88600756");
	}
	
	public String getImage(String url) throws IOException, ParseException {
		URL u = new URL(url);
		JSONParser parser = new JSONParser();
		String r = null;
		
		while(r == null) {
			r = Requests.followRedirect(getLinkPreviewURL(url));
			try {
				if (r == null) {
					Thread.sleep(timeout);	
				}

			} catch (InterruptedException e) {
				log.error(Util.stackTraceToString(e));
			}
		}
		
		JSONObject jobj = (JSONObject) parser.parse(r);
		
		return jobj.get("image").toString();
	}
	

	private URL getLinkPreviewURL(String url) throws MalformedURLException {
		return new URL("http://api.linkpreview.net/?key="+ randomAPIKey() + "&q=" + url);
	}
	
	private String randomAPIKey() { 
        Random rand = new Random(); 
        return apiKeys.get(rand.nextInt(apiKeys.size())); 
    } 
	
	public static void main(String[] argv) throws IOException, ParseException {
		String ex = "https://api.iextrading.com/1.0/stock/five/article/6687001001067890";
		ImageFromURL ifu = new ImageFromURL();
		
		String r = ifu.getImage(ex);
		System.out.println(r);
		
	}
	
	
}
