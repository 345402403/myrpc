package com.lm.rpc.netty.medium;

import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.lm.rpc.netty.annotation.Remote;

@Component
public class InitialMedium implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if(bean.getClass().isAnnotationPresent(Remote.class)) {
			Map<String, BeanMethod> beanMap = Media.beanMap;
			try {
				Method[] methods = bean.getClass().getDeclaredMethods();
				for(Method m : methods) {
					BeanMethod bm = new BeanMethod();
					bm.setMethod(m);
					bm.setBean(bean);
					beanMap.put(bean.getClass().getInterfaces()[0].getName()+"."+m.getName(), bm);
				}
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		return bean;
	}

}
