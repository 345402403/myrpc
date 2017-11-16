package com.lm.rpc.netty.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.lm.rpc.netty.entity.User;
import com.lm.rpc.netty.server.Response;
import com.lm.rpc.netty.server.ResponseUtil;

@Controller
public class UserController {
	@Resource
	private IUserService userService; 
	
	public Response saveUser(User user){
		userService.save(user);
		return ResponseUtil.createSuccessResult(user);
	}
	
	public Response saveUsers(List<User> users){
		userService.saveList(users);
		return ResponseUtil.createSuccessResult(users);
	}
}
