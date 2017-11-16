package com.lm.rpc.netty.medium;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.lm.rpc.netty.server.Response;
import com.lm.rpc.netty.server.ServerRequest;

public class Media {

	public static Map<String, BeanMethod> beanMap;

	static {
		beanMap = new HashMap<String, BeanMethod>();
	}

	private static Media media = null;

	public static Media getInstance() {
		if (media == null) {
			media = new Media();
		}
		return media;
	}

	public Response process(ServerRequest request) {
		String command = request.getCommand();
		Object content = request.getContent();
		String strContent = JSONObject.toJSONString(content);

		Response resp = null;
		BeanMethod bm = Media.beanMap.get(command);
		Object bean = bm.getBean();
		Method m = bm.getMethod();
		Class<?>[] paramType = m.getParameterTypes();
		Object[] param = new Object[paramType.length];
		for(int i = 0; i < paramType.length; i++) {
			param[i] = JSONObject.parseObject(JSONObject.parseArray(strContent).getString(i), paramType[i]);
		}
		try {
			resp = (Response) m.invoke(bean, param);
			resp.setId(request.getId());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return resp;
	}
}
