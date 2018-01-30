package com.netty.server.handler;

import java.rmi.activation.ActivationGroupDesc.CommandEnvironment;
import java.util.List;

import org.springframework.util.SerializationUtils;

import com.netty.bean.RpcRequest;
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
		System.out.println("服务端解码..");
		
		ByteBuf buf=(ByteBuf)msg;
		
		if(buf.readableBytes()<4)
		{
			System.out.println("小于4");
			return;
		}
		buf.markReaderIndex(); //标记读索引的位置 
        int length = buf.readInt();
        System.out.println("length:"+length);
        System.out.println("readablebyte:"+buf.readableBytes() );
        
       if (buf.readableBytes() < length)
       {  
           buf.resetReaderIndex();  //还原读索引的位置，如果本来是0，就回到0
           System.out.println("重置");
       }  
			
		byte [] bytes=new byte[buf.readableBytes()];//设置这个字节数组的长度为可读的字节数
		buf.readBytes(bytes);
		
		Object request=ProtostuffUtils.deserializer(bytes, RpcRequest.class);
		
//		Object request=SerializationUtils.deserialize(bytes);
		
		System.out.println("msg反序列化后的数据:"+request);
		
		list.add(request);
		
	}

}
