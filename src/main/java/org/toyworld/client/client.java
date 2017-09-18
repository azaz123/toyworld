package org.toyworld.client;
import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;



public class client {
    public Runnable writer;
    public Runnable reader;
    public SocketChannel socketchannel;
    public ByteBuffer writebuf;
    public ByteBuffer readbuf;
    private volatile LinkedList<byte[]> datalist; 
    private AtomicBoolean writingflag = new AtomicBoolean(false);
    
    public client(Runnable writer,Runnable reader,SocketChannel socket){
    	this.writer = writer;
    	this.reader = reader;
    	this.socketchannel = socket;
    	writebuf = null;
    	readbuf = ByteBuffer.allocateDirect(1024);
    	datalist = new LinkedList<byte[]>();
    }
    
    public void pushdata(byte[] data) {
    	while(!writingflag.compareAndSet(false, true)) {
    		
    	}
    	try{
    		datalist.add(data);
    	}finally{
    		writingflag.lazySet(false);
    	}
    }
    
    public byte[]  popdata() {
    	while(!writingflag.compareAndSet(false, true)) {
    		
    	}
    	byte [] rBuff = null;
    	try{
    		rBuff = datalist.removeFirst();
    	}finally{
    		writingflag.lazySet(false);
    		return rBuff;
    	}
    }
}
