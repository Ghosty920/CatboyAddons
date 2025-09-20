package im.ghosty.catboyaddons.utils.events

import cc.polyfrost.oneconfig.events.EventManager
import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import net.minecraft.network.Packet

class PacketHandler : ChannelDuplexHandler() {

    override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
        if (msg is Packet<*>) {
            val event = PacketReceiveEvent(msg)
            EventManager.INSTANCE.post(event)
            if (event.isCancelled) return;
        }
        super.channelRead(ctx, msg)
    }

    override fun write(ctx: ChannelHandlerContext?, msg: Any?, promise: ChannelPromise?) {
        if (msg is Packet<*>) {
            val event = PacketReceiveEvent(msg)
            EventManager.INSTANCE.post(event)
            if (event.isCancelled) return;
        }
        super.write(ctx, msg, promise)
    }

}