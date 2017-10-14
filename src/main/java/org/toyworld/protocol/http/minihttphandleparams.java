package org.toyworld.protocol.http;

public class minihttphandleparams {
    private minihttpreqpacket request;
    private minihttpresppacket response;
    
    public void SetReq(minihttpreqpacket data){
    	request = data;
    }
    
    public void SetResp(minihttpresppacket data){
    	response = data;
    }
    
    public minihttpreqpacket GetReq(){
    	return request;
    }
    
    public minihttpresppacket GetResp(){
    	return response;
    }
}
