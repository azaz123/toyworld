package org.toyworld.session;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

import org.toyworld.api.protocol;
import org.toyworld.api.service;
import org.toyworld.service.defaultservice;
import org.toyworld.toycontext.onecontext;

public class clientsession {
	public Runnable writer;
    public Runnable reader;
    public ByteBuffer writebuf;
    public ByteBuffer readbuf;
    public SocketChannel socketchannel;
    private volatile LinkedList<byte[]> datalist; 
    private service  ser = new defaultservice();
    public onecontext rundata = null;
	public SelectionKey keydata = null;
	public CopyOnWriteArrayList<ByteBuffer> Writeq = new CopyOnWriteArrayList<ByteBuffer>();
	public CopyOnWriteArrayList<ByteBuffer> unFinishedWriteq = new CopyOnWriteArrayList<ByteBuffer>();
	public byte[] willWrite;
    
    public clientsession(Runnable writer,Runnable reader,SocketChannel socket,onecontext rundata,SelectionKey keydata){
    	this.writer = writer;
    	this.reader = reader;
    	this.socketchannel = socket;
    	this.rundata = rundata;
    	this.keydata = keydata;
    	writebuf = null;
    	readbuf = ByteBuffer.allocate(1024);
    	datalist = new LinkedList<byte[]>();
    }
    
    public void write(byte[] data) {

    	try{
    		datalist.add(data);
    		rundata.writerequestq.add(keydata);
    	}finally{

    	}
    }
    
    public byte[]  getwillwritedata() {

    	byte [] rBuff = null;
    	try{
    		rBuff = datalist.removeFirst();
    	}finally{
    		return rBuff;
    	}
    }
    
    public service  getservice() {
    	return ser;
    }
    
    public byte[]  getreaddata() {
    	return readbuf.array();
    }
}
