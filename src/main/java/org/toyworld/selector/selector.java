package org.toyworld.selector;

import org.toyworld.Itoycomponent.Itoycomponent;
import org.toyworld.Itoycomponent.toycomponent;
import org.toyworld.toycontext.onecontext;
import java.nio.channels.SelectionKey;
import java.security.Key;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class selector extends toycomponent implements Itoycomponent {
	Consumer<SelectionKey> pushread = null;
	Consumer<SelectionKey> pushwrite = null;
	
	Predicate<SelectionKey> JudgeReadablecond1 = null;
	Predicate<SelectionKey> JudgeReadablecond2 = null;
	
	Predicate<SelectionKey> JudgeWritecond1 = null;
	Predicate<SelectionKey> JudgeWritecond2 = null;
	
	{
		
               JudgeReadablecond1 = (key) ->{
				     return key.isReadable();
				};
				
				 JudgeReadablecond2 = (key) ->{
					 return !rundata.rkeyset.contains(key.hashCode());
				};
				
				JudgeWritecond1 = (key) ->{
				     return key.isWritable();
				};
				
				JudgeWritecond2 = (key) ->{
					 return !rundata.wkeyset.contains(key.hashCode());
				};
				
				pushread = (key) -> {
					rundata.rkeyset.add(key.hashCode());
					rundata.readeventq.add(key);
				};
				
				pushwrite = (key) -> {
					rundata.wkeyset.add(key.hashCode());
					rundata.writeeventq.add(key);
				};
				
				logical = () ->{

					   Set<SelectionKey> selectedKeys = null;
					   try{
						   rundata.selector.select(500);
						   selectedKeys = rundata.selector.selectedKeys();
						   
						   if(selectedKeys.stream()
								   .filter(key -> key.isAcceptable())
								   .count()>0) {
							   rundata.getcomponent("acceptor").run();
							   selectedKeys.clear();
							   return;
						   }
							
						   selectedKeys.stream()
						   .filter(JudgeWritecond1.and(JudgeWritecond2))
						   .peek(pushwrite)
						   .count();
						   
						   selectedKeys.stream()
						   .filter(JudgeReadablecond1.and(JudgeReadablecond2))
						   .peek(pushread)
						   .count();
						   
						   
						   
						   selectedKeys.clear();
					   }catch(Exception e){
						   e.printStackTrace();
					   }
					   
					   


			};
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
	}

}
