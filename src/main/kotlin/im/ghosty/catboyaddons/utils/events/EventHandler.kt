package im.ghosty.catboyaddons.utils.events

import cc.polyfrost.oneconfig.events.EventManager
import cc.polyfrost.oneconfig.events.event.ReceivePacketEvent
import cc.polyfrost.oneconfig.events.event.SendPacketEvent
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import im.ghosty.catboyaddons.mixin.C0DPacketCloseWindowAccessor
import im.ghosty.catboyaddons.mixin.S2EPacketCloseWindowAccessor
import net.minecraft.network.play.client.C0DPacketCloseWindow
import net.minecraft.network.play.server.S2EPacketCloseWindow

object EventHandler {

    @Subscribe
    fun onReceivePacket(event: ReceivePacketEvent) {
        if (event.packet is S2EPacketCloseWindow) {
            val packet = event.packet as S2EPacketCloseWindowAccessor
            val ev = InventoryCloseEvent(packet.windowId)
            EventManager.INSTANCE.post(ev)
            if (ev.isCancelled) event.isCancelled = true
            return
        }
    }

    @Subscribe
    fun onSendPacket(event: SendPacketEvent) {
        if(event.packet is C0DPacketCloseWindow) {
            val packet = event.packet as C0DPacketCloseWindowAccessor
            val ev = InventoryCloseEvent(packet.windowId)
            EventManager.INSTANCE.post(ev)
            if (ev.isCancelled) event.isCancelled = true
            return
        }
    }

}