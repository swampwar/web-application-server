package webserver;

import java.io.IOException;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;

public class ListUserController extends AbstractController {
	
	private static final Logger log = LoggerFactory.getLogger(ListUserController.class);
	
	public void doGet(HttpRequest request, HttpResponse response) {
		try {
	        if (!request.getLogined()) { // 로그인상태가 아니면
	            response.sendRedirect("/user/login.html");
	            return;
	        }     

	        Collection<User> users = DataBase.findAll();
	        StringBuilder sb = new StringBuilder();
	        sb.append("<table border='1'>");
	        for (User user : users) {
	            sb.append("<tr>");
	            sb.append("<td>" + user.getUserId() + "</td>");
	            sb.append("<td>" + user.getName() + "</td>");
	            sb.append("<td>" + user.getEmail() + "</td>");
	            sb.append("</tr>");
	        }
	        sb.append("</table>");
	        
	        byte[] body = sb.toString().getBytes();
	        
	        response.forwardBody(body);
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpRequest request, HttpResponse response) {
		
	}

}
