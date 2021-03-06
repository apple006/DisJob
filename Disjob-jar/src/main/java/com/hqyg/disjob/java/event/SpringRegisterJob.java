package com.hqyg.disjob.java.event;

import com.hqyg.disjob.event.AbstractEventObject;
import com.hqyg.disjob.event.ObjectEvent;
import com.hqyg.disjob.event.ObjectListener;
import com.hqyg.disjob.java.job.EJob;
import com.hqyg.disjob.java.service.JobService;
import com.hqyg.disjob.java.utils.Log;
import com.hqyg.disjob.java.utils.StringUtils;

/**
 * 使用spring 启动的方式来注册job
 * @author Disjob
 *
 */
public class SpringRegisterJob extends AbstractEventObject<String>{

	@Override
	public void attachListener() {
		this.addListener(new ObjectListener<String>() {
			
			public void onEvent(ObjectEvent<String> event) {
				String className =event.getValue();
				if(StringUtils.isEmpty(className)){
					return ;
				}
				
				try {
					Class<?> clazz = Class.forName(className.trim());
					Object ejob = clazz.newInstance();
					if(!(ejob instanceof EJob)){
						return ;
					}
					JobService.initJob(className);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					Log.error(e.getMessage(),e);
				} catch (InstantiationException e) {
					e.printStackTrace();
					Log.error(e.getMessage(),e);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					Log.error(e.getMessage(),e);
				}
			}
		}, EventType.REGISTER_JOB);
	}
	
	public void notify(String className){
		ObjectEvent<String> event = new ObjectEvent<String>(className, EventType.REGISTER_JOB);
		notifyListeners(event);
	}
}
