package org.toyworld.clientregeistor;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Set;

import org.toyworld.Itoycomponent.Itoycomponent;
import org.toyworld.Itoycomponent.toycomponent;
import org.toyworld.toycontext.onecontext;
import org.toyworld.client.client;
import java.util.function.Consumer;

public class clientregeistor extends toycomponent implements Itoycomponent {
	Consumer<SocketChannel> registerfun = null;
	{
		registerfun = (sock) -> {
			
			try{
				SelectionKey s = sock.register(rundata.selector,SelectionKey.OP_WRITE | SelectionKey.OP_READ);
				Runnable writer = rundata.getcomponent("writer");
				Runnable reader = rundata.getcomponent("reader");
				client c = new client(writer,reader,sock);
				s.attach(c);
				rundata.clientq.remove(sock);
				sock.write(ByteBuffer.wrap("welecom to hrz telnet.... \r\n".getBytes()));
				System.out.println("one client connected");
			}catch(Exception ex){
				ex.printStackTrace(); 
			}
			
		};
	}
	
	{
		logical = () ->{
			  while(true)
			  {
				  try {
					  Thread.sleep(500);
					  
					  if(rundata.clientq.isEmpty())
						  continue;
					  
					  rundata.clientq.stream()
					  .peek(registerfun)
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
