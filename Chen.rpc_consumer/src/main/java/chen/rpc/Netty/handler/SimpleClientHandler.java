package chen.rpc.Netty.handler;

import com.alibaba.fastjson.JSONObject;

import chen.rpc.Netty.client.Response;
import chen.rpc.Netty.client.ResultFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class SimpleClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		if("ping".equals(msg.toString())) {
			ctx.writeAndFlush("ping\r\n");
	
		}
		System.out.println(msg.toString());
		
		Response result = JSONObject.parseObject(msg.toString(),Response.class);
		ResultFuture.receive(result);
		System.out.println(result.getCode()+","+result.getMsg());
		System.out.println("testfinal"+Thread.currentThread().getName());
		
		
		
		
				
	}

}
