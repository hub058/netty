package cn.netty.sf;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer{
	private final int port;

	public EchoServer(int port) {
		this.port = port;
	}
	
	public static void main(String[] args) throws Exception{
		int port = 8511;
		new EchoServer(port).start();
	}

	private void start() throws Exception {
		NioEventLoopGroup group = new NioEventLoopGroup();
		try {
			//与Bootstrap类包包含丰富的客户端API一样，ServerBootstrap能够非常方便的实现典型的服务端。
			ServerBootstrap b = new ServerBootstrap();
			b.group(group)
				//指定所使用的NIO传输Channel
				.channel(NioServerSocketChannel.class)
				//使用指定的端口设置套接字地址
				.localAddress(new InetSocketAddress(port))
				//添加一个EchoServerHandler 到子Channel的ChannelPipeline
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch)
							throws Exception {
						ch.pipeline().addLast(new EchoServerHandler());
					}
				});
			//新建一个future实例,异步地绑定服务器；调用sync()方法阻塞等待直到绑定完成
			ChannelFuture future = b.bind().sync();
			//获取Channel 的CloseFuture，并且阻塞当前线程直到它完成
            //该应用程序将会阻塞等待直到服务器的Channel关闭
			//（因为你在Channel 的CloseFuture 上调用了sync()方法）。
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			//关闭EventLoopGroup，释放所有的资源
			group.shutdownGracefully().sync();
		}
		
	}

}
