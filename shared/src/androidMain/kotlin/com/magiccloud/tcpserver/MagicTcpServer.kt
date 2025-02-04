package com.magiccloud.tcpserver

import android.util.Log
import com.magiccloud.dao.ByteBuf
import com.magiccloud.dao.MagicDao
import com.magiccloud.dao.MagicEd719
import com.magiccloud.dao.MagicType
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.MessageToMessageCodec

val Tag = "MagicTcpServer"
private var magicType: MagicType = MagicType.MagicEd719Type

interface OnMagicTcpistener {
    fun onStartResult(result: Boolean, e: Exception?)
    fun onStopped()
    fun onMessageReceived(type: MagicType, obj: MagicDao)
}

class MagicTcpServer(private val listener: OnMagicTcpistener) {
    private lateinit var bossGroup: NioEventLoopGroup
    private lateinit var workGroup : NioEventLoopGroup

    fun startServer(type: MagicType, port: Int) {
        magicType = type

        Thread {
            bossGroup = NioEventLoopGroup(1)
            workGroup = NioEventLoopGroup()
            try {
                val bootstrap = ServerBootstrap()
                    .group(bossGroup, workGroup)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_REUSEADDR, true)
                    .childHandler(MagicTcpServerInitializer(listener))
                val channelFunction = bootstrap.bind(port).sync()
                Log.d(Tag, "MagicTcpServer start success")
                listener.onStartResult(true, null)
                channelFunction.channel().closeFuture().sync()
            } catch (e: Exception) {
                listener.onStartResult(false, e)
            } finally {
                listener.onStopped()
            }
        }.start()
    }

    fun stopServer() {
        Log.d(Tag, "MagicTcpServer stop")
        bossGroup.shutdownGracefully()
        workGroup.shutdownGracefully()
        listener.onStopped()
    }
}
// 初始化Channel
private class MagicTcpServerInitializer(private val listener: OnMagicTcpistener) : ChannelInitializer<NioSocketChannel>() {
    override fun initChannel(ch: NioSocketChannel?) {
        ch?.pipeline()?.apply {
            addLast(MagicCodecHandle())
            addLast(MagicReceiveHandler(listener))
        }
    }

}
// 数据包解析
private class MagicCodecHandle: MessageToMessageCodec<ByteBuf, MagicDao>() {
    override fun encode(ctx: ChannelHandlerContext?, msg: MagicDao?, out: MutableList<Any>?) {
    }

    override fun decode(ctx: ChannelHandlerContext?, msg: ByteBuf?, out: MutableList<Any>?) {
        when(magicType) {
            MagicType.MagicEd719Type -> {
                val magicEd719 = MagicEd719()
                magicEd719.decode(msg!!)
                out?.add(magicEd719)
            }

            MagicType.MagicH03Type -> TODO()
        }
    }

}
// 读取数据包
private class MagicReceiveHandler(private val listener: OnMagicTcpistener) : SimpleChannelInboundHandler<MagicDao>() {
    override fun channelRead0(ctx: ChannelHandlerContext?, msg: MagicDao?) {
        listener.onMessageReceived(magicType, msg!!)
    }

}