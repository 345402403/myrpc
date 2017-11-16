package com.lm.rpc.netty.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


import com.lm.rpc.netty.consts.Constants;
import com.lm.rpc.utils.ServerConfigReader;
import com.lm.rpc.zookeeper.client.ZookeeperFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

@Component
public class SpringNettyServer implements ApplicationListener<ContextRefreshedEvent> {

	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void start() throws InterruptedException {
		int port = Integer.parseInt(ServerConfigReader.findProp("netty.port"));
		ServerBootstrap server = new ServerBootstrap();
		EventLoopGroup parentGroup = new NioEventLoopGroup();
		EventLoopGroup childGroup = new NioEventLoopGroup();
		server.group(parentGroup, childGroup);

		server.option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, false)
				.channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new DelimiterBasedFrameDecoder(65535, Delimiters.lineDelimiter()[0]));
						ch.pipeline().addLast(new StringDecoder());
						ch.pipeline().addLast(new IdleStateHandler(6, 4, 2, TimeUnit.SECONDS));
						ch.pipeline().addLast(new SimpleServerHandler());
						ch.pipeline().addLast(new StringEncoder());
					}
				});
		System.out.println("listening " + port);
		ChannelFuture futrue = server.bind(port).sync();

		//zk
		CuratorFramework curator = ZookeeperFactory.getCurator();
		try {
			InetAddress address = InetAddress.getLocalHost();
			curator.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(Constants.SERVER_PATH + address.getHostAddress()+ "#");
			System.out.println("register to zookeeper:" + Constants.SERVER_PATH + address.getHostAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		futrue.channel().closeFuture().sync();// 阻塞在这

	}

}
