import org.junit.BeforeClass;
import org.junit.Test;
import junit.framework.Assert;
import org.toyworld.niotoy.*;
public class test {
	niotoy instance  = new niotoy();
	@Test
	public void test(){
		instance.initial();
		instance.setServer(23);
		instance.bindservice();
		instance.play();
	}
}
