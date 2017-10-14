import org.junit.BeforeClass;
import org.junit.Test;
import junit.framework.Assert;
import org.toyworld.niotoy.*;
import org.toyworld.api.*;
import org.toyworld.protocol.http.*;
import org.toyworld.toymarket.*;


public class test {
	niotoy instance  = new niotoy();
	@Test
	public void test(){
		instance.initial();
		instance.setServer(1010);
		instance.bindservice("minihttp;minihttphandle");
		instance.play();
		//toymarket market= new toymarket();
		//market.initial();
	}
}
