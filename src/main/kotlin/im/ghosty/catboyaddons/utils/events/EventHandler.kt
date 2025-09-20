package im.ghosty.catboyaddons.utils.events

import cc.polyfrost.oneconfig.events.EventManager
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import im.ghosty.catboyaddons.CatboyAddons.mc
import net.minecraft.network.play.client.C0DPacketCloseWindow
import net.minecraft.network.play.server.S2EPacketCloseWindow
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent

object EventHandler {

    @Subscribe
    fun onReceivePacket(event: PacketReceiveEvent) {
        if (event.packet is S2EPacketCloseWindow) {
            val ev = InventoryCloseEvent()
            EventManager.INSTANCE.post(ev)
            if (ev.isCancelled) event.isCancelled = true
            return
        }
    }

    @Subscribe
    fun onSendPacket(event: PacketSendEvent) {
        if(event.packet is C0DPacketCloseWindow) {
            val ev = InventoryCloseEvent()
            EventManager.INSTANCE.post(ev)
            if (ev.isCancelled) event.isCancelled = true
            return
        }
    }

    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Load) {
        val ev = WorldChangeEvent(true)
        EventManager.INSTANCE.post(ev)
    }

    @Subscribe
    fun onWorldUnload(event: WorldEvent.Unload) {
        val ev = WorldChangeEvent(false)
        EventManager.INSTANCE.post(ev)
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onServerConnect(event: ClientConnectedToServerEvent) {
        if (mc.currentServerData == null) return
        event.manager.channel().pipeline().addAfter("fml:packet_handler", "catboyaddons_packet_handler", PacketHandler())
    }

}
