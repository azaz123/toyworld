package org.toyworld.reader;

import java.io.IOException;
import java.nio.channels.SelectionKey;

import org.toyworld.client.client;
import org.toyworld.toycontext.onecontext;


public class readerlogical implements Runnable {
	public onecontext rundata = null;
	public SelectionKey keydata = null;
	
	public void setrundata(onecontext data) {
		rundata = data;
	}
	
	public void setkeydata(SelectionKey key) {
		keydata = key;
	}
	
	public void run() {
		client c = (client)keydata.attachment();
		int reccount =0;
		try{
			if(c.readbuf.hasRemaining()){
				reccount = c.socketchannel.read(c.readbuf);
				if(reccount == -1){
					System.out.println("client disconnected: "+ keydata.channel().toString());
					keydata.cancel();
				}
				//push service
			}
			else{
				
				c.readbuf.clear();
				reccount = c.socketchannel.read(c.readbuf);
				//push service
				System.out.println(c.readbuf.toString());
			}
			
		}catch(IOException ex){
			
		}finally{
			rundata.rkeyset.remove(keydata.hashCode());
		}
		
	}
}
