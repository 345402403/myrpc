package com.lm.rpc.netty.client;

import com.alibaba.fastjson.JSONObject;
import com.lm.rpc.netty.server.Response;
import com.lm.rpc.utils.ServerConfigReader;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class TcpClient {

	public final Bootstrap client = new Bootstrap();
	public static ChannelFuture future = null;
	static {
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap client = new Bootstrap();
		try {
			client.group(group); // (2)
			client.channel(NioSocketChannel.class); // (3)
			client.option(ChannelOption.SO_KEEPALIVE, true); // (4)
			client.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline()
							.addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()[0]));
					ch.pipeline().addLast(new StringDecoder());
					ch.pipeline().addLast(new SimpleClientHandler());
					ch.pipeline().addLast(new StringEncoder());
				}
			});

			System.out.println("client started");
			int port = Integer.parseInt(ServerConfigReader.findProp("netty.port"));
			future = client.connect("localhost", port).sync();

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			//group.shutdownGracefully();
		}
	}
	
	
	public static Response send(ClientRequest request){
		future.channel().writeAndFlush(JSONObject.toJSONString(request));
		future.channel().writeAndFlush("\r\n");
		DefaultFuture df = new DefaultFuture(request);
		return df.get();
		
	}

}
