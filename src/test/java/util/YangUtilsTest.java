package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class YangUtilsTest {
	
	@Test
	public void bufferedReadertTest() throws IOException{
		
		/**
		 *  br.read(char[] cbuf, int off, int len)
		 *  cbuf의 off 인덱스부터 len 만큼의 character를 br로부터 읽어서 덮어 씌운다. 
		 */
		
		String str = "ABCDE";
		StringReader strReader = new StringReader(str);
		BufferedReader br = new BufferedReader(strReader);
		
		char[] rslt = "123456789".toCharArray();
		
		br.read(rslt, 2, 3);
		
		System.out.println(rslt);
		
		
	}
	

}
