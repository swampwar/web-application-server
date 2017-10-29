package util;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.Test;

import webserver.HttpMethod;
import webserver.HttpRequest;

public class HttpRequestTest {
	private String httpResDirectory = "./src/test/resource/";
	
	@Test
	public void request_GET() throws FileNotFoundException {
		
		InputStream in = new FileInputStream(new File(httpResDirectory + "Http_GET.txt"));
		
		HttpRequest request = new HttpRequest(in);
		
		assertEquals(HttpMethod.GET,request.getMethod());
		assertEquals("/user/create",request.getPath());
		assertEquals("keep-alive",request.getHeader("Connection"));
		assertEquals("testid",request.getParameter("userId"));
		assertEquals(true,request.getLogined());
	}
	
	@Test
	public void request_POST() throws FileNotFoundException {
		
		InputStream in = new FileInputStream(new File(httpResDirectory + "Http_POST.txt"));
		
		HttpRequest request = new HttpRequest(in);
		
		assertEquals(HttpMethod.POST,request.getMethod());
		assertEquals("/user/create",request.getPath());
		assertEquals("keep-alive",request.getHeader("Connection"));
		assertEquals("testid",request.getParameter("userId"));
		assertEquals("testName",request.getParameter("name"));
	}
	

}
