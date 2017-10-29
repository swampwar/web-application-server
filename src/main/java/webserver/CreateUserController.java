package webserver;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;

public class CreateUserController implements Controller {
	
	private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

	@Override
	public void service(HttpRequest request, HttpResponse response) {
		
		try {
			User user = new User(request.getParameter("userId"), request.getParameter("password"), request.getParameter("name"),request.getParameter("email"));
            log.debug("user : {}", user);
            DataBase.addUser(user); // DB에 저장
			response.sendRedirect("/index.html");
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
