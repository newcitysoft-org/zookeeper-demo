package com.newcitysoft.study.zookeeper.module.net.scene.socket.netty;

import com.alibaba.fastjson.JSONObject;
import com.newcitysoft.study.zookeeper.module.net.scene.socket.entity.DataPacket;
import com.newcitysoft.study.zookeeper.module.net.scene.socket.entity.Task;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Logger;

/**
 * @author lixin.tian@renren-inc.com
 * @date 2018/3/9 11:36
 */
public class TimeClientHandler extends ChannelHandlerAdapter {
//    private final ByteBuf firstMessage;

    private static final Logger logger = Logger.getLogger(TimeClientHandler.class.getName());

    private int counter;
    private byte[] req;

    public TimeClientHandler() {
        DataPacket<Task> packet = new DataPacket<>();
        Task task = new Task("crawl", 10);
        byte[] bytes = JSONObject.toJSONString(task).getBytes();

        packet.setType(DataPacket.PacketType.SYNCGET);
        packet.setLength(bytes.length);
        packet.setBody(task);

        req = (JSONObject.toJSONString(packet)+"$_").getBytes();

        //req = ("QUERY TIME ORDER$_").getBytes();
//        firstMessage = Unpooled.buffer(req.length);
//
//        firstMessage.writeBytes(req);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warning("Unexpected exception from downstream :" + cause.getMessage());
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 未考虑TCP粘包和拆包的情况
        // ctx.writeAndFlush(firstMessage);
        ByteBuf message = null;
        for(int i = 0; i < 100; i++) {
            message = Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);
        }

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf buf = (ByteBuf) msg;
//        byte[] req = new byte[buf.readableBytes()];
//        buf.readBytes(req);
//        String body = new String(req, "UTF-8");
        String body = (String) msg;
        System.out.println("Now is " + body + " ; the counter is : " + ++counter);
    }
}
