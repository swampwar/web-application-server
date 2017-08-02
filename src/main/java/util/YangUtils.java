package util;

import java.io.BufferedReader;
import java.io.IOException;

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

	public static String[] getDataFromURL(String url) {

        String path = "";
        String queryString = "";
    	String[] data = new String[2];
		
		int index = url.indexOf('?');
		
		if(index == -1) {
			path = url;
		}else {
			path = url.substring(0,index);
			queryString = url.substring(index+1);
		}

		log.debug("request path : {}, request params : {}",path,queryString);
		
		data[0] = path;
		data[1] = queryString;
		
		return data;
	}
	
}
