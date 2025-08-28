package im.ghosty.catboyaddons.features.f7.terms

import cc.polyfrost.oneconfig.events.event.ReceivePacketEvent
import cc.polyfrost.oneconfig.events.event.RenderEvent
import cc.polyfrost.oneconfig.events.event.Stage
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import im.ghosty.catboyaddons.Config
import im.ghosty.catboyaddons.utils.MSTimer
import im.ghosty.catboyaddons.utils.events.InventoryCloseEvent
import net.minecraft.network.play.server.S2DPacketOpenWindow

object AutoTerms {

    var firstClick = true
    var timer: MSTimer? = null
    var lastClick: ItemData? = null
    var clicked = false

    @Subscribe
    fun onUpdate(event: RenderEvent) {
        if (event.stage !== Stage.END) return
        if (Config.secure || !Config.autoTerms) return
        if (!TermHandler.inTerminal) return

        if (timer == null) {
            timer = MSTimer()
            firstClick = true
            lastClick = null
            clicked = false
            return
        }

        val delay = (if (firstClick) Config.autoTermsFirstClickDelay else Config.autoTermsClickDelay).toLong()
        if (!timer!!.hasPassed(delay)) return

        if (clicked) {
            if (lastClick == null) return
            if (!timer!!.hasPassed(Config.autoTermsBreakThreshold.toLong())) return

            if(!Config.autoTermsReverseOrder)
                TermHandler.getSolution()?.addFirst(lastClick!!)
            else TermHandler.getSolution()?.addLast(lastClick!!)

            lastClick = null
            return
        }

        val solution = TermHandler.getSolution() ?: return
        lastClick = if (!Config.autoTermsReverseOrder) solution.pollFirst() else solution.pollLast() ?: return

        firstClick = false
        timer!!.reset()
        clicked = true
    }

    @Subscribe
    fun onOpenWindow(event: ReceivePacketEvent) {
        if (event.packet !is S2DPacketOpenWindow) return
        clicked = false
    }

    @Subscribe
    fun onCloseWindow(event: InventoryCloseEvent) {
        timer = null // that will reset everything eventually
    }

}