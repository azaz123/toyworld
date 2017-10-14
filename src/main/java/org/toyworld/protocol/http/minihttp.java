package org.toyworld.protocol.http;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;

import org.toyworld.api.protocol;
import org.toyworld.api.servicehandle;
import org.toyworld.api.serviceparams;
import org.toyworld.protocol.netprotocol;
import org.toyworld.session.clientsession;


public class minihttp extends netprotocol implements protocol{

	private String name = null;
	private String value = null;
	private final static String EMPTY_VALUE = "";
	private minihttpsession session = new minihttpsession();
	private final static String headerendflag = "\r\n";
	private static final String shortDesc = "OK";
    /**
     * Horizontal space
     */
	private static final byte SP = 32;

    /**
     * Horizontal tab
     */
	private static final byte HT = 9;

    /**
     * Carriage return
     */
	private static final byte CR = 13;

    /**
     * Equals '='
     */
	private static final byte EQUALS = 61;

    /**
     * Line feed character
     */
	private static final byte LF = 10;

    /**
     * Colon ':'
     */
	private static final byte COLON = 58;

    /**
     * Semicolon ';'
     */
	private static final byte SEMICOLON = 59;

    /**
     * Comma ','
     */
	private static final byte COMMA = 44;

    /**
     * Double quote '"'
     */
	private static final byte DOUBLE_QUOTE = '"';
	
	private static final short CRLF_SHORT = (CR << 8) | LF;
	
    private void splitHeader(String sb) {
        final int length = sb.length();
        int nameStart;
        int nameEnd;
        int colonEnd;
        int valueStart;
        int valueEnd;
        name = null;
        value = null;

        nameStart = findNonWhitespace(sb, 0);
        for (nameEnd = nameStart; nameEnd < length; nameEnd ++) {
            char ch = sb.charAt(nameEnd);
            if (ch == ':' || Character.isWhitespace(ch)) {
                break;
            }
        }

        for (colonEnd = nameEnd; colonEnd < length; colonEnd ++) {
            if (sb.charAt(colonEnd) == ':') {
                colonEnd ++;
                break;
            }
        }

        name = sb.substring(nameStart, nameEnd);
        name.replaceAll("\r|\n", "");
        valueStart = findNonWhitespace(sb, colonEnd);
        if (valueStart == length) {
            value = EMPTY_VALUE;
        } else {
            valueEnd = findEndOfString(sb);
            value = sb.substring(valueStart, valueEnd);
            value.replaceAll("\r|\n", "");
        }
    }
	
	private static int findNonWhitespace(String sb,int offset) {
        for (int result = offset; result < sb.length(); ++result) {
            if (!Character.isWhitespace(sb.charAt(result))) {
                return result;
            }
        }
        return sb.length();
    }
	
	private static int findWhitespace(String sb, int offset) {
        for (int result = offset; result < sb.length(); ++result) {
            if (Character.isWhitespace(sb.charAt(result))) {
                return result;
            }
        }
        return sb.length();
    }
	
	private static int findEndOfString(String sb) {
        for (int result = sb.length() - 1; result > 0; --result) {
            if (!Character.isWhitespace(sb.charAt(result))) {
                return result + 1;
            }
        }
        return 0;
    }
	
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
    
    private static String[] splitInitialLine(String line) {
    	byte[] tmp = line.getBytes();
    	int aStart;
        int aEnd;
        int bStart;
        int bEnd;
        int cStart;
        int cEnd;

        aStart = findNonWhitespace(line, 0);
        aEnd = findWhitespace(line, aStart);

        bStart = findNonWhitespace(line, aEnd);
        bEnd = findWhitespace(line, bStart);

        cStart = findNonWhitespace(line, bEnd);
        cEnd = findEndOfString(line);
        return new String[] {
        		line.substring(aStart, aEnd),
        		line.substring(bStart, bEnd),
                cStart < cEnd? line.substring(cStart, cEnd) : "" };
    }
    
    private decodestate readheader(ByteBuffer buf){
    	decodestate rstate = null;
    	//find non line,rollback buffer
    	String line = readline(buf);
	    if(null == line){
	    	return null;
	    }
	    if(line.length() > 0){
	    	do {
	    		splitHeader(line);
	    		if(!name.isEmpty()){
	    			session.currentPacket.Getheader().put(name, value);
	    		}
	    		
	    		line = readline(buf);
                if (line == null) {
                    return null;
                }
	    	} while(!line.equals(headerendflag));
	    }
	    rstate = decodestate.READ_FIXED_LENGTH_CONTENT;
	    String contentlenStr = session.currentPacket.Getheader().get("Content-Length");
	    if(contentlenStr!=null){
	    	session.contentLength = Integer.parseInt( contentlenStr );
	    }
	    
    	return rstate;
    }
    
    private void paraseparams(minihttpreqpacket packet){
    	try{
    		Map<String, String> params = packet.Getparams();
        	String url = packet.GetUri();
        	String [] paramsline = url.split("\\?");

        	if(paramsline[0] != null){
        		packet.SetSource(paramsline[0].substring(paramsline[0].lastIndexOf("/")+1));
        	}
        	
        	if(paramsline.length>=2 && paramsline[1] != null){
        	    String key = null;
        	    String value = null;
        		int currpos = 0;
        		char[] valuearray = new char[512];
        		int arraypos = 0;
        		char [] src = paramsline[1].toCharArray();
        		while(currpos < src.length){
        		    char charvalue = src[currpos];
        		    if(charvalue == '='){
        		       key = String.copyValueOf(valuearray,0,arraypos);
        		       arraypos = 0;
        		       currpos++;
        		       continue;
        		    }

        		    if(charvalue == '&'){
        		       value = String.copyValueOf(valuearray,0,arraypos);
        		       arraypos = 0;
        		       currpos++;
        		       if(key != null && value != null && !key.isEmpty() && !value.isEmpty()){
           		    	    params.put(key, value);
           		    	    key = null;
           		    	    value = null;
           		    	    continue;
           		       }
        		    }
        		    
        		   
        		    valuearray[arraypos] = charvalue;
        		    arraypos++;
        		    currpos++;
        		}
        		
        		if(arraypos>0){
        			value = String.copyValueOf(valuearray,0,arraypos);
        		}
        		
        		if(key != null && value != null && !key.isEmpty() && !value.isEmpty()){
    		    	    params.put(key, value);
    		    	    key = null;
    		    	    value = null;
    		       }
        		
        	}
        	System.out.println("resource: " + packet.GetResource() + "   param: " +  params.toString());
    	}catch(Exception ex){
    	     ex.printStackTrace();
    	}
    }
    
    private int readcontent(ByteBuffer buf){
    	int rlength = 0;
    	byte[] newData = buf.slice().array();
    	//find non line,rollback buffer
    	rlength = session.currentPacket.CopyBody(newData);
    	buf.position(buf.position() + rlength);
    	return rlength;
    }
    
    private void Decode(){
	    switch(session.currentstate){
	        case SKIP_CONTROL_CHARS:{
	    	    if(!skipControlCharacters(session.currentbuf)){
	    	    	session.currentstate = decodestate.SKIP_CONTROL_CHARS;
	    	    	return;
	    	    }
	    	    else{
	    	    	session.currentstate = decodestate.READ_INITIAL;
	    	    }
	        }
            case READ_INITIAL:{
            	String line = readline(session.currentbuf);
	    	    if(null == line){
	    	    	return;
	    	    }
	    	    else {
	    	    	String[] initial = splitInitialLine(line);
	    	    	if(initial.length < 3){
	    	    		session.currentstate = decodestate.SKIP_CONTROL_CHARS;
		    	    	return;
	    	    	}
	    	    	session.currentPacket.SetMethod(initial[0]);
	    	    	session.currentPacket.SetUri(initial[1]);
	    	    	session.currentPacket.SetVersion(initial[2]);
	    	    	paraseparams(session.currentPacket);
	    	    	session.currentstate = decodestate.READ_HEADER;
	    	    }
	        }
            case READ_HEADER:{
            	decodestate nextstate = readheader(session.currentbuf);
    	    	if(null == nextstate){
    	    		return;
    	    	}
    	    	else {
    	    		session.currentstate = decodestate.READ_FIXED_LENGTH_CONTENT;
    	    	}
	        }
            case READ_FIXED_LENGTH_CONTENT:{
            	int lastbyte = 0;
            	int totalbyte = session.contentLength;
    	    	if(session.contentLength == 0){
    	    		session.currentstate = decodestate.SKIP_CONTROL_CHARS;
    	    		session.hasPacket = true;
    	    		return;
    	    	}else {
    	    		int copylength = readcontent(session.currentbuf);
    	    		lastbyte = session.contentLength - copylength;
    	    		if(lastbyte > 0){
    	    			return;
    	    		} else {
    	    			session.contentLength = 0;
    	    			session.currentstate = decodestate.SKIP_CONTROL_CHARS;
    	    			session.hasPacket = true;
    	    		}
    	    	}
	        }
	    }
	}
    
    private void Encode(minihttpresppacket in,serviceparams params){
    	
    	ByteBuffer operBuff = ByteBuffer.wrap(new byte[4096]);
    	
    	operBuff.put(in.GetVer().getBytes());
    	operBuff.put(SP);
    	operBuff.put(in.GetStatus().getBytes());
    	operBuff.put(SP);
    	operBuff.put(shortDesc.getBytes());
    	operBuff.putShort(CRLF_SHORT);
    	
    	Iterator iter = in.GetHeader().keySet().iterator();
        while(iter.hasNext()){
           String name = (String)iter.next();
     	   String value = (String)in.GetHeader().get(name);
     	   operBuff.put(name.getBytes());
     	   operBuff.put(COLON);
     	   operBuff.put(SP);
     	   operBuff.put(value.getBytes());
     	   operBuff.putShort(CRLF_SHORT);
        }
        operBuff.putShort(CRLF_SHORT);
        operBuff.put(in.GetBody(), 0, in.GetBodySize());
        operBuff.flip();
        
        params.out = new byte[operBuff.remaining()];
        System.arraycopy(operBuff.array(), operBuff.position(), params.out, 0, operBuff.remaining());
        System.out.println(params.out.length);
        System.out.println(new String(params.out));
    }
	
	@Override
	public void Excute(serviceparams params) {
		// TODO Auto-generated method stub
		byte[] in = params.session.getreaddata();
		session.makenewbuf(in);
		Decode();
		if(session.hasPacket){
			minihttphandleparams handleparams = new minihttphandleparams();
			handleparams.SetReq(session.currentPacket);
			handleparams.SetResp(new minihttpresppacket());
			params.bindObj = handleparams;
			session.currentPacket.printstring();
			super.shandle.Excute(params);
			session.hasPacket = false;
			session.currentPacket.clear();
			Encode(handleparams.GetResp(),params);
			System.out.println(params.out.length);
			params.session.write(params.out);
			handleparams.GetResp().printstring();
		}
	}
	
	@Override
	public void Bindhandle(servicehandle handle){
    	this.shandle = handle;
    }


}
