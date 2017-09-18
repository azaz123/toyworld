package org.toyworld.controler;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import org.toyworld.Itoycomponent.Itoycomponent;
import org.toyworld.Itoycomponent.toycomponent;
import org.toyworld.client.client;
import java.util.function.Consumer;
import org.toyworld.toycontext.onecontext;
import org.toyworld.writer.writerlogical;
import org.toyworld.reader.readerlogical;


import java.io.BufferedReader;  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileReader;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
import java.io.RandomAccessFile;  
import java.io.Reader; 




public class controler extends toycomponent implements Itoycomponent {
	Consumer<SelectionKey> pushreaderfun = null;
	Consumer<SelectionKey> pushwriterfun = null;
	Consumer<SelectionKey> reActiveread = null;
	Consumer<SelectionKey> reActivewrite = null;
	
	{
		pushreaderfun = (key) -> {
			client c = (client)key.attachment();
			key.interestOps(key.interestOps() & ~SelectionKey.OP_READ);
			rundata.rTrack.add(key);
			rundata.readeventq.remove(key);
			readerlogical t = (readerlogical)c.reader;
			t.setkeydata(key);
/*			
			ByteBuffer tmp = ByteBuffer.wrap("test".getBytes());
			System.out.println(tmp.toString());
			try{
				c.socketchannel.write(tmp);
				System.out.println(tmp.toString());
			}catch(IOException ex){
				
			}
	*/		
			rundata.executor.execute(c.reader);
		};
		
		pushwriterfun = (key) -> {
			client c = (client)key.attachment();
			key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
			rundata.wTrack.add(key);
			rundata.writeeventq.remove(key);
			writerlogical t = (writerlogical)c.writer;
			t.setkeydata(key);
			rundata.executor.execute(c.writer);
		};
		
		reActiveread = (key) -> {
			key.interestOps(key.interestOps() | SelectionKey.OP_READ);
		};
		
		reActivewrite = (key) -> {
			key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
		};
		
		logical = () ->{
			  while(true)
			  {
				  try {
					  Thread.sleep(500);
					  
					  rundata.readeventq.stream()
					  .peek(pushreaderfun)
					  .count();
					  
					  

					  
					  
					  rundata.writeeventq.stream()
					  .filter(key -> rundata.writerequestq.contains(key))
					  .filter(key -> key.isValid())
					  .peek(pushwriterfun)
					  .count();
					  
					  rundata.rTrack.stream()
					  .filter(key -> (!rundata.rkeyset.contains(key.hashCode())))
					  .filter(key -> key.isValid())
	                .peek(reActiveread)
	                .count();
					  
					  rundata.wTrack.stream()
					  .filter(key -> (!rundata.wkeyset.contains(key.hashCode())))
					  .filter(key -> key.isValid())
	                .peek(reActivewrite)
	                .count();

					  
			        } catch (InterruptedException e) {
			            e.printStackTrace(); 
			        }
				  
			  }

		};
	}
	
	
	@Override
	public Runnable getRunnable() {
		// TODO Auto-generated method stub
		return logical;
	}

	@Override
	public void bindData(onecontext data) {
		// TODO Auto-generated method stub
		rundata = data;
	}

}
