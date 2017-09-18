package org.toyworld.acceptor;

import org.toyworld.Itoycomponent.Itoycomponent;
import org.toyworld.Itoycomponent.toycomponent;
import org.toyworld.toycontext.onecontext;
import java.nio.channels.SocketChannel;
import java.nio.channels.SocketChannel;
import java.io.IOException;

public class acceptor extends toycomponent implements Itoycomponent {
    
	{
		logical = () ->{
			try{
				SocketChannel socketChannel = rundata.serversocketchannel.accept();
				socketChannel.configureBlocking(false);
				rundata.clientq.put(socketChannel);
			}catch(IOException ex){
				
			}catch(InterruptedException ex){
				
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
