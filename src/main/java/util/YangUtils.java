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
    	
    	return url;
    }

    @Test
	public void getPathParams() {
//        String url = "/user/create?userId=123&userPw=pwpw";
        String url = "/user/create";
        
    	String[] pathParams = new String[2];
		
		int index = url.indexOf('?');
		if(index == -1){
			index = url.length()-1;
		}
		
		String path = url.substring(0,index);
		String params = url.substring(index+1,url.length());
		
		log.debug("path : {}, params : {}",path,params);
		
		
	}
}
