package im.ghosty.catboyaddons.mixin;

import cc.polyfrost.oneconfig.events.EventManager;
import im.ghosty.catboyaddons.utils.events.PacketReceiveEvent;
import im.ghosty.catboyaddons.utils.events.PacketSendEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SideOnly(Side.CLIENT)
@Mixin(value = NetworkManager.class, priority = Integer.MAX_VALUE)
public class NetworkManagerMixin {
	
	@Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
	protected void channelRead0(ChannelHandlerContext chc, Packet<?> packet, CallbackInfo ci) {
		PacketReceiveEvent event = new PacketReceiveEvent(packet);
		EventManager.INSTANCE.post(event);
		if(event.isCancelled) ci.cancel();
	}
	
	@Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
	public void sendPacket(Packet<?> packet, CallbackInfo ci) {
		PacketSendEvent event = new PacketSendEvent(packet);
		EventManager.INSTANCE.post(event);
		if(event.isCancelled) ci.cancel();
	}
	
	@Inject(method = "sendPacket(Lnet/minecraft/network/Packet;Lio/netty/util/concurrent/GenericFutureListener;[Lio/netty/util/concurrent/GenericFutureListener;)V", at = @At("HEAD"), cancellable = true)
	public void sendPacket(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>> listener, GenericFutureListener<? extends Future<? super Void>>[] listeners, CallbackInfo ci) {
		PacketSendEvent event = new PacketSendEvent(packet);
		EventManager.INSTANCE.post(event);
		if(event.isCancelled) ci.cancel();
	}
	
}
