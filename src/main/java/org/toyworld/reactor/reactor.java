package org.toyworld.reactor;

import org.toyworld.Itoycomponent.Itoycomponent;
import org.toyworld.Itoycomponent.toycomponent;
import org.toyworld.toycontext.onecontext;

public class reactor extends toycomponent implements Itoycomponent {
	{
		logical = () ->{
			   while(true)
			   {
				   try{
					   rundata.getcomponent("selector").run();
					   Thread.sleep(500);
				   }catch(Exception e){
					   e.printStackTrace();
				   }
				   
			   }

		};
	}
	
	
	@Override
	public void bindData(onecontext data) {
		// TODO Auto-generated method stub
		
		rundata = data;
	}
	
	@Override
	public Runnable getRunnable() {
		// TODO Auto-generated method stub
		
		return logical;
	}

}
