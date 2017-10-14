package org.toyworld.publicmodule;

import org.toyworld.session.clientsession;
import org.toyworld.toycontext.onecontext;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class sessionmgr {
    public static clientsession createsession(onecontext rundata,SelectionKey key,SocketChannel sock){
    	Runnable writer = rundata.getcomponent("writer");
		Runnable reader = rundata.getcomponent("reader");
    	return new clientsession(writer,reader,sock,rundata,key);
    }
    
    public static void freesession(clientsession session){
    	session.keydata.cancel();
    	session.readbuf.clear();
    	session.unFinishedWriteq.clear();
    	session.Writeq.clear();
    }
}

