package org.toyworld.toycontext;
import java.nio.channels.ServerSocketChannel; 
import java.nio.channels.SocketChannel; 
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.toyworld.Itoycomponent.Itoycomponent;
import java.util.concurrent.CopyOnWriteArrayList;
/**
 * toy context
 * @author hu.ruizhe
 *
 */
public class onecontext {
    public ServerSocketChannel serversocketchannel;
    public BlockingQueue<SocketChannel> clientq;  
    public BlockingQueue<SelectionKey> readeventq;
    public BlockingQueue<SelectionKey> writeeventq;
    public BlockingQueue<SelectionKey> writerequestq;
    public ConcurrentSkipListSet<Integer> wkeyset;
    public ConcurrentSkipListSet<Integer> rkeyset;
    public Selector selector;
    private HashMap<String, Itoycomponent> allcomponent;
    public  ExecutorService executor;
    public  CopyOnWriteArrayList<SelectionKey> wTrack;
    public  CopyOnWriteArrayList<SelectionKey> rTrack;
    
    
    {
    	clientq = new ArrayBlockingQueue<SocketChannel>(1024);
    	readeventq = new ArrayBlockingQueue<SelectionKey>(1024);
    	writeeventq = new ArrayBlockingQueue<SelectionKey>(1024);
    	writerequestq = new ArrayBlockingQueue<SelectionKey>(1024);
    	wkeyset = new ConcurrentSkipListSet<Integer>();
    	rkeyset = new ConcurrentSkipListSet<Integer>();
    	wTrack = new CopyOnWriteArrayList<SelectionKey>();
    	rTrack = new CopyOnWriteArrayList<SelectionKey>();
    	executor = Executors.newFixedThreadPool(10);
    }
    
    public void setcomponet(HashMap<String, Itoycomponent> data)
    {
    	allcomponent = data;
    }
    
    public void createserver(int bindport)
    {
    	try{
    		serversocketchannel = ServerSocketChannel.open();
    		serversocketchannel.configureBlocking(false);
    		InetSocketAddress address = new InetSocketAddress(bindport);
    		serversocketchannel.socket().bind(address);
    		selector = Selector.open();
    		serversocketchannel.register(selector,SelectionKey.OP_ACCEPT);
    		System.out.println("server start at " + address);
    	}catch(IOException ex){
    		
    	}
    	
    }
    
    public Runnable getcomponent(String name)
    {
        Runnable r = allcomponent.get(name).getRunnable();
        return r;
    	
    }
}
