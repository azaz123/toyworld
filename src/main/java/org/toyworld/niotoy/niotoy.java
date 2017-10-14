package org.toyworld.niotoy;

import java.util.HashMap;
import org.toyworld.Itoycomponent.*;
import org.toyworld.toycontext.*;
import org.toyworld.reactor.*;
import org.toyworld.selector.*;
import org.toyworld.acceptor.*;
import org.toyworld.clientregeistor.*;
import org.toyworld.controler.*;
import org.toyworld.writer.*;
import org.toyworld.reader.*;
import org.toyworld.api.service;



public class niotoy {
	private onecontext context;
	private HashMap<String, Itoycomponent> allcomponent = new HashMap<String, Itoycomponent>();
	
	public void initial(){
		context = new onecontext();
		Itoycomponent reactor = new reactor();
		Itoycomponent selector = new selector();
		Itoycomponent acceptor = new acceptor();
		Itoycomponent clientregeistor = new clientregeistor();
		Itoycomponent controler = new controler();
		Itoycomponent writer = new writer();
		Itoycomponent reader = new reader();
		
		reactor.bindData(context);
		selector.bindData(context);
		acceptor.bindData(context);
		clientregeistor.bindData(context);
		controler.bindData(context);
		writer.bindData(context);
		reader.bindData(context);
		
		allcomponent.put("reactor", reactor);
		allcomponent.put("selector", selector);
		allcomponent.put("acceptor", acceptor);
		allcomponent.put("clientregeistor", clientregeistor);
		allcomponent.put("controler", controler);
		allcomponent.put("writer", writer);
		allcomponent.put("reader", reader);
		
		context.setcomponet(allcomponent);
	
	}
	
	public void setServer(int port){
		context.createserver(port);
	}
	
	public void bindservice(String skey){
		context.skey = skey;
	}
	
    public void play(){
    	Thread reactort=new Thread(allcomponent.get("reactor").getRunnable());  
        Thread clientregeistort=new Thread(allcomponent.get("clientregeistor").getRunnable());
        Thread controler=new Thread(allcomponent.get("controler").getRunnable());
        reactort.start();
        clientregeistort.start();
        controler.start();
        
        while(true){
        	 try{
				   Thread.sleep(500);
			   }catch(Exception e){
				   e.printStackTrace();
			   }
        }
	}
}


