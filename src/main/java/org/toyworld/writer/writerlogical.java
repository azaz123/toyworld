package org.toyworld.writer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Iterator;
import java.util.function.Consumer;

import org.toyworld.client.client;
import org.toyworld.toycontext.onecontext;



public class writerlogical implements Runnable {

	public onecontext rundata = null;
	public SelectionKey keydata = null;
	public CopyOnWriteArrayList<ByteBuffer> Writeq = new CopyOnWriteArrayList<ByteBuffer>();
	public CopyOnWriteArrayList<ByteBuffer> unFinishedWriteq = new CopyOnWriteArrayList<ByteBuffer>();
	private byte[] willWrite; 
	private Consumer<ByteBuffer> unFinishedFunc = null;
	private Consumer<ByteBuffer> unWiteqFunc = null;
	private client c = null;
	
	
	
	{
		
		unWiteqFunc = (b) ->{
			try{
				c.socketchannel.write(b);
				if(!b.hasRemaining()){
					Writeq.remove(b);
					//b.flip();
					Writeq.stream()
					.filter(d -> d.hasRemaining())
					.limit(1)
					.peek(unWiteqFunc)
					.count();
				}
				else{
					unFinishedWriteq.add(b);
					Writeq.remove(b);
				}
			}catch(IOException ex){
				
			}
			
		};
		
		unFinishedFunc = (b) ->{
			try{
				c.socketchannel.write(b);
				if(!b.hasRemaining()){
					//b.flip();
					Writeq.stream()
					.filter(d -> d.hasRemaining())
					.limit(1)
					.peek(unWiteqFunc)
					.count();
					
					unFinishedWriteq.remove(b);
				}
			}catch(IOException ex){
				
			}
			
		};
	}
	
	public void setrundata(onecontext data) {
		rundata = data;
	}
	
	public void setkeydata(SelectionKey key) {
		keydata = key;
		c = (client)keydata.attachment();
		willWrite = c.popdata();
	}
	

	public void run() {
		int nRet = 0;
		client c = (client)keydata.attachment();
		try{
			Writeq.add(ByteBuffer.wrap(willWrite));
			if(unFinishedWriteq.stream().count() == 0){
				
				Writeq.stream()
				.filter(d -> d.hasRemaining())
				.limit(1)
				.peek(unWiteqFunc)
				.count();
			}
			else{
				unFinishedWriteq.stream()
				.filter(b -> b.hasRemaining())
				.limit(1)
				.peek(unFinishedFunc)
				.count();
			}
			

		}finally{
			rundata.wkeyset.remove(keydata.hashCode());
			if(Writeq.isEmpty() && unFinishedWriteq.isEmpty()){
				rundata.writerequestq.remove(keydata);
			}
		}
		
	}

}
