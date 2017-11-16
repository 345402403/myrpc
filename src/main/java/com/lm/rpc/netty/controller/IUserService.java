package com.lm.rpc.netty.controller;

import java.util.List;

import com.lm.rpc.netty.entity.User;

public interface IUserService {
	public void save(User user);

	public void saveList(List<User> users);
}
