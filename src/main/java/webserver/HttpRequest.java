package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import util.HttpRequestUtils;
import util.IOUtils;

public class HttpRequest {
	
	private Map<String, String> reqHeaderMap = new HashMap<String,String>();
	private Map<String, String> reqParamMap = new HashMap<String,String>();
	private String method;
	private String path;

	public HttpRequest(InputStream in) {
		
		try {
		    BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
		
			String line = br.readLine();
			
			String[] firstLine = line.split(" ");
			this.method = firstLine[0];
			this.path = firstLine[1];
			
			while(true) {
				line = br.readLine();
				if(line == null || "".equals(line)) break;
				
				reqHeaderMap.put(line.substring(0, line.indexOf(':')).trim(), line.substring(line.indexOf(':')+1).trim());
			}
			
			if("GET".equals(this.getMethod())) {
				if(this.path.indexOf('?') != -1) {	// QueryString이 있으면
					String queryString = this.path.substring(this.path.indexOf('?')+1);
					
					reqParamMap = HttpRequestUtils.parseQueryString(queryString);
					this.path = this.path.substring(0, this.path.indexOf('?'));
				}
				
			}else if("POST".equals(this.getMethod())) {
				String body = IOUtils.readData(br, Integer.parseInt(reqHeaderMap.get("Content-Length")));
				reqParamMap = HttpRequestUtils.parseQueryString(body);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public String getMethod() {
		return this.method;
	}
	
	public String getPath() {
		return this.path;
	}
	
	public String getHeader(String key) {
		return reqHeaderMap.get(key);
	}
	
    public String getParameter(String key) {
		return reqParamMap.get(key);
	}

}
