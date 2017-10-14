package org.toyworld.clientregeistor;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Set;

import org.toyworld.Itoycomponent.Itoycomponent;
import org.toyworld.Itoycomponent.toycomponent;
import org.toyworld.toycontext.onecontext;
import java.util.function.Consumer;
import org.toyworld.api.protocol;
import org.toyworld.api.servicehandle;
import org.toyworld.api.serviceparams;
import org.toyworld.protocol.http.minihttp;
import org.toyworld.protocol.http.minihttphandleparams;
import org.toyworld.session.clientsession;
import org.toyworld.publicmodule.*;

public class clientregeistor extends toycomponent implements Itoycomponent {
	Consumer<SocketChannel> registerfun = null;
	{
		registerfun = (sock) -> {
			
			try{
				SelectionKey s = sock.register(rundata.selector,SelectionKey.OP_WRITE | SelectionKey.OP_READ);
				clientsession session = sessionmgr.createsession(rundata, s, sock);
				servicemgr.bindservice(rundata, session);
				//sock.write(ByteBuffer.wrap("welecome hrz telnet server...".getBytes()));
				s.attach(session);
				rundata.clientq.remove(sock);
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
