package webserver;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {
	
	private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
	private HashMap<String,String> headerMap = new HashMap<String,String>();
	private DataOutputStream dos;

	public HttpResponse(OutputStream creatOutputStream) {
		dos = new DataOutputStream(creatOutputStream);
	}
	
	public void forward(String url) throws IOException {
		
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        
        if (url.endsWith(".css")) {
        	addHeader("Content-Type", "text/css;charset=utf-8");
        }else if(url.endsWith(".js")){
        	addHeader("Content-Type", "application/javascript");
        }else {
        	addHeader("Content-Type", "text/html;charset=utf-8");
        }
        
        addHeader("Content-Length", Integer.toString(body.length)); // dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        
        response200Header();
        responseBody(body);
		
	}
	
	public void forwardBody(byte[] body) throws IOException {
		
		addHeader("Content-Type", "text/html;charset=utf-8");
		addHeader("Content-Length", Integer.toString(body.length));
		
        response200Header();
        responseBody(body);
		
	}
	
	public void sendRedirect(String url) throws IOException {
		
		dos.writeBytes("HTTP/1.1 302 Found \r\n");
		addHeader("Location", url); // dos.writeBytes("Location: /index.html \r\n");
		
		processHeaders();

		byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
		responseBody(body);
		
	}
    
    private void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    
    private void response200Header() throws IOException {
    	
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        processHeaders();

    }
    
    private void processHeaders() throws IOException {
    	
        Set<String> keySet = headerMap.keySet();
        StringBuffer sb = new StringBuffer();
        for(String key : keySet) {
        	sb.append(key).append(": ").append(headerMap.get(key)).append(" \r\n");
        	dos.writeBytes(sb.toString());
        }
        
        dos.writeBytes("\r\n");
    	
    }
    
	public void addHeader(String key,String value) {
		this.headerMap.put(key, value);
	}

}
