package org.toyworld.writer;

import org.toyworld.Itoycomponent.Itoycomponent;
import org.toyworld.Itoycomponent.toycomponent;
import org.toyworld.toycontext.onecontext;




public class writer extends toycomponent implements Itoycomponent {
    

	@Override
	public Runnable getRunnable() {
		// TODO Auto-generated method stub
		logical = new writerlogical();
		return logical;
	}

	@Override
	public void bindData(onecontext data) {
		// TODO Auto-generated method stub
		rundata = data;
	}

}
