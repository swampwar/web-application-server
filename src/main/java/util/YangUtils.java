package util;

import java.io.BufferedReader;
import java.io.IOException;

public class YangUtils {
	
	
    public static String getUrl(String line) throws IOException {
    	
    	String[] strArray = line.split(" ");
    	String url = strArray[1];
    	
    	return url;
    }
}
