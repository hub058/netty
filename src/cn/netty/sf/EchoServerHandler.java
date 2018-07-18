package cn.netty.sf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
/**
 * 因为你的Echo 服务器会响应传入的消息，所以它需要实现ChannelInboundHandler 接口，用
 * 来定义响应入站事件的方法。
 */
//标示一个ChannelHandler 可以被多个Channel 安全地共享
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

	//对于每个传入的消息都会被调用；
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		//将消息记录到控制台
		ByteBuf in = (ByteBuf) msg;
		System.out.println("Server Received:"+in.toString(CharsetUtil.UTF_8));
		ctx.write(in);
	}
	
	//通知ChannelInboundHandler最后一次对channelRead()
    //的调用是当前批量读取中的最后一条消息
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	
}
