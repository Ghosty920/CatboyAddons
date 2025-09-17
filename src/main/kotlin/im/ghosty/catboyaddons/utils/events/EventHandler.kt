package im.ghosty.catboyaddons.utils.events

import cc.polyfrost.oneconfig.events.EventManager
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import net.minecraft.network.play.client.C0DPacketCloseWindow
import net.minecraft.network.play.server.S2EPacketCloseWindow
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

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

}