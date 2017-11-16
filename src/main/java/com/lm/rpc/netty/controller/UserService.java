package com.lm.rpc.netty.controller;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lm.rpc.netty.entity.User;

@Service
public class UserService implements IUserService{

	public void save(User user) {
		// TODO Auto-generated method stub
		//访问mysql
		System.out.println("save");
		
	}

	public void saveList(List<User> users) {
		// TODO Auto-generated method stub
		System.out.println("saveList");
	}

}
