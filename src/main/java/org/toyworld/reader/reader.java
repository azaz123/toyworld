package org.toyworld.reader;

import org.toyworld.Itoycomponent.Itoycomponent;
import org.toyworld.Itoycomponent.toycomponent;
import org.toyworld.toycontext.onecontext;



public class reader extends toycomponent implements Itoycomponent {
	
	
	@Override
	public Runnable getRunnable() {
		// TODO Auto-generated method stub
		logical = new readerlogical();
		return logical;
	}

	@Override
	public void bindData(onecontext data) {
		// TODO Auto-generated method stub
		rundata = data;
	}

}
