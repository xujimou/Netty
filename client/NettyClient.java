package com.netty.client;

import com.netty.bean.RpcRequest; 
import com.netty.client.handler.ClientResolveHandler;
import com.netty.client.handler.DecodeHandler;
import com.netty.client.handler.EncodeHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {

	
	public void connect(String host,int port){
		
		EventLoopGroup group=new NioEventLoopGroup();
		Bootstrap bootstrap=new Bootstrap();
		
		bootstrap.group(group)
		.channel(NioSocketChannel.class)
		.option(ChannelOption.SO_KEEPALIVE, true)
		.handler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline()
				.addLast(new EncodeHandler()) //编码
				.addLast(new DecodeHandler()) //解码
				.addLast(new ClientResolveHandler()) //输入流 处理
				;
			}
		});
		try{
			
			RpcRequest request=new RpcRequest();
			request.setInterfaceName("service");
			request.setRequestId("uuid12345");
		//连接服务器
			ChannelFuture cf=bootstrap.connect(host,port).sync();
			cf.channel().writeAndFlush(request);
			
			cf.channel().closeFuture().sync();
			group.shutdownGracefully().sync();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		NettyClient client=new NettyClient();
		client.connect("127.0.0.1", 20000);
	}
}
