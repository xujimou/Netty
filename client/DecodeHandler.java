package com.netty.client.handler;

import java.util.List;

import org.springframework.util.SerializationUtils;

import com.netty.bean.RpcResponse;
import com.util.CommUtil;
import com.util.ProtostuffUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class DecodeHandler extends ByteToMessageDecoder{

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> list) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("客户端解码..");
		
		ByteBuf buf=(ByteBuf)msg;
		if(buf.readableBytes()<4)
		{
			System.out.println("小于4");
			return;
		}
		buf.markReaderIndex();  
        int length = buf.readInt();  
       if (buf.readableBytes() < length)
       {  
           buf.resetReaderIndex();  
           System.out.println("重置");
       }  
		
		byte [] bytes=new byte[buf.readableBytes()];
		buf.readBytes(bytes);
		
		
//		Object response=SerializationUtils.deserialize(bytes);
		
		RpcResponse response=ProtostuffUtils.deserializer(bytes, RpcResponse.class);
		
		System.out.println("msg反序列化后的数据:"+response);
		
		list.add(response);
		//ctx.fireChannelRead(list);
		
	}

}
