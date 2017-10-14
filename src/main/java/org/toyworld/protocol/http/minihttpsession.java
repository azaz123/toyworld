package org.toyworld.protocol.http;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class minihttpsession {  
	   
	   public decodestate currentstate = decodestate.SKIP_CONTROL_CHARS;
	   
	   public ByteBuffer currentbuf = null;
	   
	   public minihttpreqpacket currentPacket = new minihttpreqpacket();
	   
	   public int contentLength = 0;
	   
	   public boolean hasPacket = false;
	   
	   public void makenewbuf(byte[] newdata){
		   if(currentbuf == null){
			   currentbuf = ByteBuffer.wrap(newdata);
		   }else{
			   ByteBuffer oldBuf = currentbuf.slice();
			   int newLength = newdata.length + oldBuf.array().length;
			   byte[] newArray = new byte[newLength];
			   System.arraycopy(oldBuf.array(), oldBuf.position(), newArray, 0, oldBuf.array().length);
			   System.arraycopy(newdata, 0, newArray, oldBuf.array().length, newdata.length);
			   currentbuf = ByteBuffer.wrap(newArray);
		   }
	   }
}
