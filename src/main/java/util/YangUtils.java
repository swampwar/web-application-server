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

		log.debug("request method : {}, request path : {}, request params : {}",reqMethod,path,queryString);
		
		data[0] = reqMethod;
		data[1] = path;
		data[2] = queryString;
		
		return data;
	}
	
}
