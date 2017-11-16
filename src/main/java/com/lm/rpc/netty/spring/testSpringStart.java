package com.lm.rpc.netty.spring;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.stereotype.Component;

@Component
public class testSpringStart implements ApplicationListener<ApplicationContextEvent>{

	@Override
	public void onApplicationEvent(ApplicationContextEvent event) {
		// TODO Auto-generated method stub
		
	}

}
