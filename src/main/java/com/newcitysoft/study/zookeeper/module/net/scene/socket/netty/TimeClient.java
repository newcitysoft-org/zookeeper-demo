package com.newcitysoft.study.zookeeper.module.net.scene.socket.netty;

import com.newcitysoft.study.zookeeper.module.net.scene.socket.Const;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author lixin.tian@renren-inc.com
 * @date 2018/3/9 11:29
 */
public class TimeClient {

    public void connect(String host, int port) {
        // 配置客户端NIO线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 解决TCP粘包导致的读半包或者多包问题的原因
                            //socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            // 定义分隔符
                            ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
                            socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                            //消息长度固定
//                            socketChannel.pipeline().addLast(new FixedLengthFrameDecoder(1024));
                            socketChannel.pipeline().addLast(new StringDecoder());
                            socketChannel.pipeline().addLast(new TimeClientHandler());
                        }
                    });

            // 发起异步连接操作
            ChannelFuture f = b.connect(host, port).sync();
            // 等待客户端链路关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        int port = Const.NETTY_PORT;
        new TimeClient().connect("127.0.0.1", port);
    }
}
