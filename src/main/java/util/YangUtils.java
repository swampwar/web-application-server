package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.RequestHandler;

public class YangUtils {
	private static final Logger log = LoggerFactory.getLogger(YangUtils.class);
	
    public static String getUrl(String line) throws IOException {
    	
    	String[] strArray = line.split(" ");
    	String url = strArray[1];
    	
    	log.debug("request header : {} ", line);
    	log.debug("request url : {} ", url);
    	
    	return url;
    }

	public static String[] getDataFromURL(String httpHeaderLine) {

        String path = "";
        String queryString = "";
        String reqMethod = "";
    	String[] data = new String[3];
    	String[] strArray = httpHeaderLine.split(" ");
    	
    	reqMethod = strArray[0];
		
		int index = strArray[1].indexOf('?');
		if(index == -1) {
			path = strArray[1];
		}else {
			path = strArray[1].substring(0,index);
			queryString = strArray[1].substring(index+1);
		}
		
		data[0] = reqMethod;
		data[1] = path;
		data[2] = queryString;
		
		return data;
	}

	public static HashMap getDataFromHttpRequest(BufferedReader br,boolean logFlag) throws IOException {
		
		HashMap<String,String> rsltData = new HashMap<String,String>();
		String line = br.readLine();
		br.mark(1);
		int lineNum = 1;
		String[] dataFromLine1 = null;
		
		while(line != null && !"".equals(line)) {
			if(logFlag) log.debug(line);
			
			if(lineNum == 1) {
				dataFromLine1 = getDataFromURL(line);
				
				rsltData.put("method", dataFromLine1[0]);
				rsltData.put("path", dataFromLine1[1]);
				rsltData.put("params", dataFromLine1[2]);
				
				lineNum++;
			}
			
			if(line.startsWith("Content-Length")) {
    			String contentLength = line.split(":")[1].trim();
    			rsltData.put("contentLength", contentLength);
    		}else if(line.startsWith("Cookie")) {
    			String cookie = line.split(":")[1].trim(); // Cookie : logined=true
    			rsltData.put("logined", isLogin(cookie));
    		}
			
			line = br.readLine();
		}
		
		br.reset();
	
		return rsltData;
		
	}
	
	public static String isLogin(String line) {
		String isLogin = line.split("=")[1].trim();
		
		return isLogin;
	}
	
}
