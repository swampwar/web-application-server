package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractController implements Controller{
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

	@Override
	public void service(HttpRequest request, HttpResponse response) {
		HttpMethod httpMethod = request.getMethod();
		
		if(httpMethod.isPost()) {
			doPost(request, response);
		}else if(httpMethod.isGet()) {
			doGet(request, response);
		}else {
			log.debug("HttpMethod Error !");
		}
	}
	
	protected abstract void doPost(HttpRequest request, HttpResponse response);
	protected abstract void doGet(HttpRequest request, HttpResponse response);
}
