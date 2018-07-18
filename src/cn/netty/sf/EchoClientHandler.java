package cn.netty.sf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.CharsetUtil;

//响应客户端请求的处理器
@Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

	//连接激活的时候调用
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks 火箭", CharsetUtil.UTF_8));
	}
	
	//接受到数据的时候调用
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg)
			throws Exception {
		System.out.println("Client Received:"+msg.toString(CharsetUtil.UTF_8));
	}
	
	//发生异常的时候调用
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
