package com.lm.rpc.netty.remote;

import java.util.List;

import javax.annotation.Resource;

import com.lm.rpc.netty.annotation.Remote;
import com.lm.rpc.netty.controller.IUserService;
import com.lm.rpc.netty.entity.User;
import com.lm.rpc.netty.server.Response;
import com.lm.rpc.netty.server.ResponseUtil;

@Remote
public class UserRemoteImpl implements UserRemote {

	@Resource
	private IUserService userService; 
	
	public Response saveUser(User user,int i){
		userService.save(user);
		return ResponseUtil.createSuccessResult(user);
	}
	
	public Response saveUsers(List<User> users){
		userService.saveList(users);
		return ResponseUtil.createSuccessResult(users);
	}

}
