package org.toyworld.protocol.http;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class minihttpresppacket {
	private Map<String, String> headers = new HashMap<String, String>();
	private byte[] bodydata = new byte[4096];
	private String version;
	private String status;
	private int responselen = 0;
	private int bodysize = 0;
	
	public String GetStatus(){
		return status;
	}
	
	public String GetVer(){
		return version;
	}
	
	public int GetLen(){
		return responselen;
	}

	
	public Map<String, String> GetHeader(){
		return headers;
	}
	
	public byte[] GetBody(){
		return bodydata;
	}
	
	public int GetBodySize(){
		return bodysize;
	}
	
	public void SetStatus(String status){
		this.status = status;
	}
	
	public void SetVer(String ver){
		this.version = ver;
	}
	
	public void SetLen(int len){
		this.responselen = len;
	}
	
	public void SetBodySize(int size){
		bodysize = size;
	}
	
	public void printstring(){
        String str = null;
        str = "status line"+ "\r\n\r\n" + version + "," + status + "," +"OK" + "\r\n\r\n";
        str = str + "header" +"\r\n\r\n";
        Iterator iter = headers.keySet().iterator();
        while(iter.hasNext()){
     	   Object name = iter.next();
     	   Object value = headers.get(name);
     	   str = str + name + "=" + value + "\r\n";
        }
        str = "\r\n\r\n" + str + "bodydata" + "\r\n" + bodydata;
        System.out.println(str);
        
	   }
	
}
