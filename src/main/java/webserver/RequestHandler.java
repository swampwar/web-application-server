package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Map;

import model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequestUtils;
import util.IOUtils;
import db.DataBase;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            
        	HttpRequest request = new HttpRequest(in);
        	HttpResponse response = new HttpResponse(out);
        	
            String url = getDefaultUrl(request.getPath()); // path가 없으면 index.html 디폴트세팅
            Controller controller = RequestMapping.getController(url);
            
            if(controller != null) {
            	controller.service(request, response);
            }else {
            	response.forward(url);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String getDefaultUrl(String path) {
        if (path.equals("/")) {
            path = "/index.html";
        }
        return path;
    }
    
}
