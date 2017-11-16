package com.lm.rpc.netty.client;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lm.rpc.netty.server.Response;

public class DefaultFuture {

	public static ConcurrentHashMap<Long, DefaultFuture> ALLFUTURE = new ConcurrentHashMap<Long, DefaultFuture>(); // all

	private Lock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();

	private Response response;

	public DefaultFuture(ClientRequest request) {
		ALLFUTURE.put(request.getId(), this);
	}

	public Response get() {
		lock.lock();
		try {
			while (!done()) {
				try {
					condition.await();// 等待数据返回
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return response;
	}

	private boolean done() {
		if (response == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 处理服务器返回值
	 * 
	 * @param response
	 */
	public static void receive(Response response) {
		DefaultFuture df = DefaultFuture.ALLFUTURE.get(response.getId());
		if (df != null) {
			Lock lk = df.lock;
			lk.lock();
			try {
				df.setResponse(response);
				df.condition.signal();
				DefaultFuture.ALLFUTURE.remove(df);
			} finally {
				lk.unlock();
			}
		}
		df.condition.signal();
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}
}
