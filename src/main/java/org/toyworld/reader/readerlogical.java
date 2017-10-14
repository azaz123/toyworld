package org.toyworld.reader;

import java.io.IOException;
import java.nio.channels.SelectionKey;

import org.toyworld.session.clientsession;
import org.toyworld.toycontext.onecontext;
import org.toyworld.api.serviceparams;


public class readerlogical implements Runnable {
	public clientsession sessiondata = null;

	
	public void setsessiondata(clientsession sdata) {
		sessiondata = sdata;
	}
	
	public void run() {
		int reccount =0;
		try{
			if(sessiondata.readbuf.hasRemaining()){
				reccount = sessiondata.socketchannel.read(sessiondata.readbuf);
				if(reccount == -1){
					System.out.println("client disconnected: "+ sessiondata.keydata.channel().toString());
					sessiondata.keydata.cancel();
				}else {
					//push service
					serviceparams param = new serviceparams();
					param.session = sessiondata;
					sessiondata.getservice().Excute(param);
				}

			}
			else{
				
				sessiondata.readbuf.clear();
				reccount = sessiondata.socketchannel.read(sessiondata.readbuf);
				System.out.println(sessiondata.readbuf.toString());
				serviceparams param = new serviceparams();
				param.session = sessiondata;
				sessiondata.getservice().Excute(param);
			}
			
		}catch(IOException ex){
			ex.printStackTrace();
			sessiondata.keydata.cancel();
		}finally{
			sessiondata.rundata.rkeyset.remove(sessiondata.keydata.hashCode());
		}
		
	}
}
