package org.toyworld.writer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Iterator;
import java.util.function.Consumer;

import org.toyworld.toycontext.onecontext;
import org.toyworld.session.clientsession;



public class writerlogical implements Runnable {

	public clientsession sessiondata = null;
	private Consumer<ByteBuffer> unFinishedFunc = null;
	private Consumer<ByteBuffer> unWiteqFunc = null;
	
	
	
	{
		
		unWiteqFunc = (b) ->{
			try{
				sessiondata.socketchannel.write(b);
				if(!b.hasRemaining()){
					sessiondata.Writeq.remove(b);
					//b.flip();
					sessiondata.Writeq.stream()
					.filter(d -> d.hasRemaining())
					.limit(1)
					.peek(unWiteqFunc)
					.count();
				}
				else{
					sessiondata.unFinishedWriteq.add(b);
					sessiondata.Writeq.remove(b);
				}
			}catch(IOException ex){
				
			}
			
		};
		
		unFinishedFunc = (b) ->{
			try{
				sessiondata.socketchannel.write(b);
				if(!b.hasRemaining()){
					//b.flip();
					sessiondata.Writeq.stream()
					.filter(d -> d.hasRemaining())
					.limit(1)
					.peek(unWiteqFunc)
					.count();
					
					sessiondata.unFinishedWriteq.remove(b);
				}
			}catch(IOException ex){
				
			}
			
		};
	}
	
	
	public void setsessiondata(clientsession sdata) {
		sessiondata = sdata;
	}
	

	public void run() {
		int nRet = 0;
		try{
			sessiondata.willWrite = sessiondata.getwillwritedata();
			sessiondata.Writeq.add(ByteBuffer.wrap(sessiondata.willWrite));
			if(sessiondata.unFinishedWriteq.stream().count() == 0){
				
				sessiondata.Writeq.stream()
				.filter(d -> d.hasRemaining())
				.limit(1)
				.peek(unWiteqFunc)
				.count();
			}
			else{
				sessiondata.unFinishedWriteq.stream()
				.filter(b -> b.hasRemaining())
				.limit(1)
				.peek(unFinishedFunc)
				.count();
			}
			

		}finally{
			sessiondata.rundata.wkeyset.remove(sessiondata.keydata.hashCode());
			if(sessiondata.Writeq.isEmpty() && sessiondata.unFinishedWriteq.isEmpty()){
				sessiondata.rundata.writerequestq.remove(sessiondata.keydata);
			}
		}
		
	}

}
