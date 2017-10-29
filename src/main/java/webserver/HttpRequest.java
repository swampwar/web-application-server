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
	private HttpMethod method; // GET or POST
	private String path; // 요청URL
	private boolean logined;

	public HttpRequest(InputStream in) {
		
		try {
		    BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
		
			String line = br.readLine();
			
			/**
			 *  HTTP요청 헤더의 첫번째 라인에서 method와 path를 추출한다. 
			 */
			String[] firstLine = line.split(" ");
			this.method = method.valueOf(firstLine[0]);
			this.path = firstLine[1];
			
			/**
			 *  HTTP요청 헤더를 파싱하여 reqHeaderMap에 저장한다. 
			 */
			while(true) {
				line = br.readLine();
				if(line == null || "".equals(line)) break;
				
				reqHeaderMap.put(line.substring(0, line.indexOf(':')).trim(), line.substring(line.indexOf(':')+1).trim());
			}
			
			/**
			 *  method에 따라 parameter를 추출하여 reqParamMap에 저장한다.
			 */
			if(this.method.isGet()) { // GET 방식 요청에서
				if(this.path.indexOf('?') != -1) {	// QueryString이 있으면
					String queryString = this.path.substring(this.path.indexOf('?')+1);
					
					reqParamMap = HttpRequestUtils.parseQueryString(queryString); // parameter 추출
					this.path = this.path.substring(0, this.path.indexOf('?')); // path 재정의('?' 이전 까지만)
				}
				
			}else if(this.method.isPost()) { // POST 방식 요청에서
				String body = IOUtils.readData(br, Integer.parseInt(reqHeaderMap.get("Content-Length"))); 
				reqParamMap = HttpRequestUtils.parseQueryString(body); // body의 parameter 추출
			}
			
			/**
			 *  reqHeaderMap의 Cookie에서 로그인여부를 확인한다.
			 */
			this.logined = isLogin(reqHeaderMap.get("Cookie")); // 로그인여부 세팅
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public HttpMethod getMethod() {
		return this.method;
	}
	
	public String getPath() {
		return this.path;
	}
	
	public boolean getLogined() {
		return this.logined;
	}
	
	public String getHeader(String key) {
		return reqHeaderMap.get(key);
	}
	
    public String getParameter(String key) {
		return reqParamMap.get(key);
	}
    
    private boolean isLogin(String cookie) {
        Map<String, String> cookies = HttpRequestUtils.parseCookies(cookie);
        String value = cookies.get("logined");
        if (value == null) {
            return false;
        }
        return Boolean.parseBoolean(value);
    }

}
