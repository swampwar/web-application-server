package webserver;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;

public class LoginController extends AbstractController {
	
	private static final Logger log = LoggerFactory.getLogger(LoginController.class);

	@Override
	protected void doPost(HttpRequest request, HttpResponse response) {
		try {
	        User user = DataBase.findUserById(request.getParameter("userId"));
	        
	        if (user != null) { // DB에 UserId가 있으면
	            if (user.login(request.getParameter("password"))) { // PW가 맞으면
	            	log.debug("login success !!");
	                response.addHeader("Set-Cookie", "logined=true");
	                response.sendRedirect("/index.html");
	            } else { // PW가 틀리면
	            	response.sendRedirect("/user/login_failed.html");
	            }
	        } else { // DB에 UserId가 없으면
	            response.sendRedirect("/user/login_failed.html");
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	protected void doGet(HttpRequest request, HttpResponse response) {

	}

}
