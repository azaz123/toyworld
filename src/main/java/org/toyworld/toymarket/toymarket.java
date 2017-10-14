package org.toyworld.toymarket;

import org.toyworld.beans.conf.*;
import org.toyworld.protocol.netprotocol;
import org.toyworld.util.YamlUtil;
import org.toyworld.api.protocol;
import org.toyworld.api.servicehandle;
import org.toyworld.api.serviceparams;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import java.lang.Class;                

import java.lang.reflect.Constructor; 

import java.lang.reflect.Field;        

import java.lang.reflect.Method;

import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;

class defaultprotocol extends netprotocol implements protocol{

	
	@Override
	public void Excute(serviceparams params) {
		// TODO Auto-generated method stub
        System.out.println("defaultprotocol");
	}
	
	@Override
	public void Bindhandle(servicehandle handle){
    	this.shandle = handle;
    }


}

class defaulthandle implements servicehandle{
	public void Excute(serviceparams params){
		System.out.println("defaulthandle");
	}
}

public class toymarket {
    private componentbean moduleconfig;
    private Map<String,Class> potocolclasspool = new HashMap<String,Class>();
    private Map<String,Class> handleclasspool = new HashMap<String,Class>();
    public void initial(){
    	try{
    		moduleconfig = YamlUtil.load("marketcomconf.yml", componentbean.class);
    		System.out.println(moduleconfig.toString());
    		moduleconfig.getprotocolmodules().stream()
    		.peek(
    				x->{
    					try{
    						for (String key : x.keySet()) {
  						      Class clazz = Class.forName(x.get(key));
  						      potocolclasspool.put(key, clazz);
  						  }
    					}catch(Exception e){
    						
    					}
    					 
    					
    				}
    				
    				)
    		.count();
    		
    		moduleconfig.gethandlemodules().stream()
    		.peek(
    				x->{
    					try{
    						for (String key : x.keySet()) {
  						      Class clazz = Class.forName(x.get(key));
  						      handleclasspool.put(key, clazz);
  						  }
    					}catch(Exception e){
    						e.printStackTrace();
    					}
    					 
    					
    				}
    				
    				)
    		.count();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    }
    
    public protocol getprotocolmodule(String name){
    	protocol rdata = null;
    	try{
    		if(name == null){
    			rdata = new defaultprotocol();
    			return rdata;
    		}
    		Class tmpclass = potocolclasspool.get(name);
    		if(tmpclass == null){
    			rdata = new defaultprotocol();
    		}else {
    			rdata = (protocol)potocolclasspool.get(name).newInstance();
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return rdata;
    }
    
    public servicehandle gethandlemodule(String name){
    	servicehandle rdata = null;
    	try{
    		if(name == null){
    			rdata = new defaulthandle();
    			return rdata;
    		}
    		Class tmpclass = handleclasspool.get(name);
    		if(tmpclass == null){
    			rdata = new defaulthandle();
    		}else{
    			rdata = (servicehandle)handleclasspool.get(name).newInstance();
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return rdata;
    }
}
