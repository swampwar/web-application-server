package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import webserver.HttpResponse;

public class HttpResponseTest {
	private String testDir = "./src/test/resources/";
	
	public void responseFoward() throws Exception{
		// HTTP_Foward.txt 결과는 응답 body에 index.html이 포함되어야 한다.
		HttpResponse response = new HttpResponse(creatOutputStream("HTTP_Foward.txt"));
		
		response.forward("/index.html");
		
		
	}
	
	private FileOutputStream creatOutputStream(String fileName) throws FileNotFoundException {
		return new FileOutputStream(new File(testDir + fileName));
	}
	
	

}
