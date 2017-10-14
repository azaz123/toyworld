package org.toyworld.protocol.line;

import java.nio.ByteBuffer;

import org.toyworld.api.protocol;
import org.toyworld.api.servicehandle;
import org.toyworld.api.serviceparams;
import org.toyworld.protocol.netprotocol;
import org.toyworld.session.clientsession;

public class liner extends netprotocol implements protocol {

	private boolean skipControlCharacters(ByteBuffer buf){
    	boolean r = false;
    	char tmp = '\0';
    	int count = 0;
    	while(true){
    		tmp = buf.getChar();
    		count++;
    		if (!Character.isISOControl(tmp)  && !Character.isWhitespace(tmp)) {
    			if(count == 1){
    				buf.position(buf.position()-2);
    			}
                r = true;
                break;
            }
    	}
    	return r;
    }
	
	private String readline(ByteBuffer buf){
    	boolean findline = false;
    	int totalcounts = buf.remaining();
    	String line = null;
    	int searchcount = 0;
    	byte[] temp = new byte[buf.remaining()];
    	byte onebyte = '\0';
    	while(true){
    		onebyte = buf.get();
    		temp[searchcount] = onebyte;
    		searchcount++;
    		if(searchcount > totalcounts-1){
    			break;
    		}
    		if( onebyte == '\n'){
    			byte[] newbyte = new byte[searchcount];
    			System.arraycopy(temp, 0, newbyte, 0, searchcount);
    			line = new String(newbyte);
    			findline = true;
    			break;
    		}
    	}
    	if(!findline){
    		buf.position(buf.position() - searchcount);
    	}
    	return line;
    }
	
	@Override
	public void Excute(serviceparams params) {
		// TODO Auto-generated method stub
		byte[] in = params.session.getreaddata();
        ByteBuffer input = ByteBuffer.wrap(in);
        boolean r = skipControlCharacters(input);
        if(!r){
        	return;
        }
        String line = readline(input);
        if(line != null){
        	params.bindObj = line;
        	super.shandle.Excute(params);;
        	params.session.write(params.out);
        	params.session.rundata.writerequestq.add(params.session.keydata);
        }
	}

	@Override
	public void Bindhandle(servicehandle handle) {
		// TODO Auto-generated method stub
		this.shandle = handle;
	}

}
