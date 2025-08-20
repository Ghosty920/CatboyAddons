package im.ghosty.catboyaddons.utils

import cc.polyfrost.oneconfig.events.event.ReceivePacketEvent
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import im.ghosty.catboyaddons.CatboyAddons.mc
import im.ghosty.catboyaddons.utils.Utils.removeFormatting
import im.ghosty.catboyaddons.utils.Utils.sendToChat
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.play.client.C0EPacketClickWindow
import net.minecraft.network.play.server.S2DPacketOpenWindow
import net.minecraft.network.play.server.S2FPacketSetSlot

object LeapHelper {

    var leapPlayer: EntityPlayer? = null
    var menuOpened = false
    var inProgress = false
    var clickedLeap = false
    var windowId: Int? = null

    fun leapTo(player: EntityPlayer) {
        val slot = ItemUtils.getHotbarSlotByItemId("INFINITE_SPIRIT_LEAP")
            ?: ItemUtils.getHotbarSlotByItemId("SPIRIT_LEAP")

        if (slot == null) {
            sendToChat("§cMissing Spirit Leap in hotbar")
            return
        }

        sendToChat("§aLeaping to §f${player.displayName.formattedText}")
        this.inProgress = true
        leapPlayer = player
        mc.thePlayer!!.inventory.currentItem = slot
        Reflection.callMethod(mc, "rightClickMouse", "func_147121_ag")
        this.clickedLeap = true
    }

    @Subscribe(priority = 1000069)
    fun onPacket(event: ReceivePacketEvent) {
        if (leapPlayer == null) return
        if (event.packet is S2DPacketOpenWindow) {
            val packet = event.packet as S2DPacketOpenWindow
            val title = packet.windowTitle.unformattedText.removeFormatting()
            if (title != "Spirit Leap") return

            menuOpened = true
            clickedLeap = false
            windowId = packet.windowId
            event.isCancelled = true
        } else if (event.packet is S2FPacketSetSlot) {
            if (!menuOpened) return
            val packet = event.packet as S2FPacketSetSlot
            val item = packet.func_149174_e()
            val windowId = packet.func_149175_c()
            val slot = packet.func_149173_d()

            if (item == null || this.windowId != windowId) return

            if (slot > 35) {
                sendToChat("§cCould not leap to §f${leapPlayer!!.displayName.formattedText}")
                stop()
                return
            }
            event.isCancelled = true

            val itemName = item.displayName.removeFormatting()
            if (itemName != leapPlayer!!.name) return

            mc.thePlayer!!.sendQueue.addToSendQueue(C0EPacketClickWindow(windowId, slot, 0, 0, null, 0))
            stop()
        }
    }

    private fun stop() {
        this.menuOpened = false
        this.leapPlayer = null
        this.inProgress = false
    }

}