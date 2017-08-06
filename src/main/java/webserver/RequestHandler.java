package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;
import util.IOUtils;
import util.YangUtils;



public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
        	
        	BufferedReader br = new BufferedReader(new InputStreamReader(in,StandardCharsets.UTF_8));
        	Map<String,String> httpRequsetMap = YangUtils.getDataFromHttpRequest(br,false);
        	
        	// Method에 따라 Parameter 추출
        	Map<String,String> parameterMap = Maps.newHashMap();
        	
        	if("GET".equals(httpRequsetMap.get("method"))) {
        		parameterMap = HttpRequestUtils.parseQueryString(httpRequsetMap.get("params"));
        	}else if("POST".equals(httpRequsetMap.get("method"))) {
            	parameterMap = HttpRequestUtils.parseQueryString(
            			IOUtils.readData(br, Integer.parseInt(httpRequsetMap.get("contentLength"))));
        	}
        	
        	// model.User에 저장
        	User user = new User(parameterMap.get("userId"),parameterMap.get("password"),
        			parameterMap.get("name"),parameterMap.get("email"));
        	
        	log.debug(user.toString());
        	
        	/**
        	 * 	리턴
        	 */
        	DataOutputStream dos = new DataOutputStream(out);
        	byte[] body = null;
        	DataBase userDB = new DataBase();
        	
        	if(httpRequsetMap.get("path").equals("/user/create")) {
        		log.debug("회원가입(/user/create), UserId : {}" + user.getUserId());
        		
        		userDB.addUser(user);
        		
        		body = Files.readAllBytes(new File("./webapp"+"/index.html").toPath());
        		response302Header(dos,"/index.html");
        	}else if(httpRequsetMap.get("path").equals("/user/login")) {
        		log.debug("로그인(/user/login), UserId : {}" , user.getUserId());
        		
        		User dbUser = userDB.findUserById(user.getUserId());
        		
        		if(dbUser != null) { // 로그인 성공
        			body = Files.readAllBytes(new File("./webapp"+"/index.html").toPath());
        			response302LoginSuccessHeader(dos,"/index.html");
        			
        		}else { // 로그인 실패
        			body = Files.readAllBytes(new File("./webapp"+"/user/login_failed.html").toPath());
        			response200Header(dos, body.length);
        		}
        		
        	}else if(httpRequsetMap.get("path").equals("/user/list")){
        		
        		String logined = httpRequsetMap.get("logined");
        		if("true".equals(logined)) {
        			Collection<User> userList = userDB.findAll();
        			StringBuffer sb = new StringBuffer("");
        			sb.append("<table border='1'>");
        			
        			for(User dbUser :userList) {
        				sb.append("<tr>");
        				sb.append("<td>" + dbUser.getUserId() + "</td>");
        				sb.append("<td>" + dbUser.getName() + "</td>");
        				sb.append("<td>" + dbUser.getEmail() + "</td>");
        				sb.append("</tr>");
        			}
        			
        			sb.append("</table>");
        			
        			body = sb.toString().getBytes();
        			response200Header(dos,body.length);
        			
        		}else {
        			body = Files.readAllBytes(new File("./webapp"+"/user/login.html").toPath());
            		response200Header(dos, body.length);
        		}
        		
        		
        	}else if(httpRequsetMap.get("path").endsWith(".css")){
        		body = Files.readAllBytes(new File("./webapp"+httpRequsetMap.get("path")).toPath());
        		response200CSSHeader(dos, body.length);
        		
        	}else {
        		body = Files.readAllBytes(new File("./webapp"+httpRequsetMap.get("path")).toPath());
        		response200Header(dos, body.length);
        	}
        	
            responseBody(dos, body);
            
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    
    private void response200CSSHeader(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/css \r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    
    private void response302Header(DataOutputStream dos, String url) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + url + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    
    private void response302LoginSuccessHeader(DataOutputStream dos, String url) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Set-Cookie: logined=true \r\n");
            dos.writeBytes("Location: " + url + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
