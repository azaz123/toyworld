package org.toyworld.protocol.http;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class minihttpreqpacket {
	private Map<String, String> headers = new HashMap<String, String>();
	private Map<String, String> parms = new HashMap<String, String>();
	private byte[] bodydata = new byte[4096];
	private String method;
	private String uri;
	private String version;
	private String resource;
	private int currentbodypos;
	private static final int maxbody = 4096;
	
	public Map<String, String> Getheader(){
		return headers;
	}
	
	public Map<String, String> Getparams(){
		return parms;
	}
	
	public byte[] GetBody(){
		return bodydata;
	}
	
	public String GetResource(){
		return resource;
	}
	
	public String GetMethod(){
		return method;
	}
	
	public String GetUri(){
		return uri;
	}
	
	public String GetVersion(){
		return version;
	}
	
	public  void SetHeader(Map<String, String> headers){
		this.headers = headers;
	}
	
	public  void SetParams(Map<String, String> parms){
		this.parms = parms;
	}
	
	public void SetBody(byte[] bodydata){
		this.bodydata = bodydata;
	}
	
	public void SetSource(String resource){
		this.resource = resource;
	}
	
	public void SetMethod(String method){
		this.method = method;
	}
	
	public void SetUri(String uri){
		this.uri = uri;
	}
	
	public void SetVersion(String version){
		this.version = version;
	}
	
	public int CopyBody(byte[] newData){
		int copylength = 0;
		if(currentbodypos < maxbody){
           if(newData.length <= (maxbody-currentbodypos)){
        	   copylength = newData.length;
           }else{
        	   copylength = maxbody-currentbodypos;
           }
		}
		System.arraycopy(newData, 0,bodydata, currentbodypos, copylength);
		currentbodypos = currentbodypos + copylength;
		return copylength;
	}
	
	public void clear(){
		headers.clear();
		parms.clear();
		currentbodypos = 0;
	}
	
	   public void printstring(){
           String str = null;
           str = "request line"+ "\r\n\r\n" + method + "," + uri + "," +version + "\r\n\r\n";
           str = str + "header" +"\r\n\r\n";
           Iterator iter = headers.keySet().iterator();
           while(iter.hasNext()){
        	   Object name = iter.next();
        	   Object value = headers.get(name);
        	   str = str + name + "=" + value + "\r\n";
           }
           
           System.out.println(str);
           
	   }
	
}
