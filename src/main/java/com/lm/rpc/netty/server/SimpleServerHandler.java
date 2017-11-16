package com.lm.rpc.netty.server;

import com.alibaba.fastjson.JSONObject;
import com.lm.rpc.netty.medium.Media;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class SimpleServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		// 业务逻辑处理
		//ctx.channel().attr(AttributeKey.valueOf("val")).set(msg);
		//ctx.channel().writeAndFlush("is ok");
		
		ServerRequest request = JSONObject.parseObject(msg.toString(), ServerRequest.class);
		Response response = Media.getInstance().process(request);
		
		ctx.channel().writeAndFlush(JSONObject.toJSONString(response));
		ctx.channel().writeAndFlush("\r\n");
		
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent idleEvt = (IdleStateEvent) evt;
			if (idleEvt.state().equals(IdleState.READER_IDLE)) {
				System.out.println("读空闲");
				ctx.channel().close();
			} else if (idleEvt.state().equals(IdleState.WRITER_IDLE)) {
				System.out.println("读空闲");
				ctx.channel().close();
			} else if (idleEvt.state().equals(IdleState.ALL_IDLE)) {
				System.out.println("读写空闲");
				ctx.channel().writeAndFlush("ping\r\n");
			}
		}
	}

}
