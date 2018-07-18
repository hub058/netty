package cn.netty.sf;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

//客户端实例
public class EchoClient {
	private final String host;
    private final int port;
    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }
    
    public static void main(String[] args) throws Exception {
		new EchoClient("localhost",8511).start();
	}
    
    public void start() throws Exception{
    	NioEventLoopGroup group = new NioEventLoopGroup();
    	try {
    		//Bootstrap类包提供包含丰富API的帮助类，能够非常方便的实现典型的服务器端和客户端通道初始化功能
			Bootstrap b = new Bootstrap();
			b.group(group)
				//使用默认的channelFactory创建一个channel
				.channel(NioSocketChannel.class)
				//定义远程地址
				.remoteAddress(host, port)
				//绑定自定义的EchoClientHandler到ChannelPipeline上
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch)
							throws Exception {
						ch.pipeline().addLast(new EchoClientHandler());
					}
				});
			//同步式的链接
			ChannelFuture cf = b.connect().sync();
			cf.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			group.shutdownGracefully().sync();
		}
	}
}
