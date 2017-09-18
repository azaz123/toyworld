package org.toyworld.reader;

import org.toyworld.Itoycomponent.Itoycomponent;
import org.toyworld.Itoycomponent.toycomponent;
import org.toyworld.toycontext.onecontext;



public class reader extends toycomponent implements Itoycomponent {
     {
    	logical = new readerlogical();
    }
	
	
	@Override
	public Runnable getRunnable() {
		// TODO Auto-generated method stub
		return logical;
	}

	@Override
	public void bindData(onecontext data) {
		// TODO Auto-generated method stub
		rundata = data;
		readerlogical tmp = (readerlogical)logical;
		tmp.setrundata(rundata);
	}

}
