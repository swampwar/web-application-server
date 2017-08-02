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
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
        	
        	BufferedReader br = new BufferedReader(new InputStreamReader(in,StandardCharsets.UTF_8));
        	
        	String line = br.readLine();
        	String url = YangUtils.getUrl(line);

        	// request의 URL에서 path, parameter 추출
        	url = YangUtils.getUrl(line);
        	String[] reqeustData = YangUtils.getDataFromURL(url);
        	
        	Map<String,String> reqMap = HttpRequestUtils.parseQueryString(reqeustData[1]);
        	User user = new User(reqMap.get("userId"),reqMap.get("password"),reqMap.get("name"),reqMap.get("email"));
        	
        	log.debug(user.toString());
        	
        	while(!"".equals(line)) {
        		line = br.readLine();
        	}
        	
        	if(url.startsWith("/user/create")) {
        		log.debug(IOUtils.readData(br, 40));
        	}
        	
        	// 3. 요청URL에 해당되는 파일을 전달
        	byte[] body = Files.readAllBytes(new File("./webapp"+reqeustData[0]).toPath());
        	
            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, body.length);
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

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
