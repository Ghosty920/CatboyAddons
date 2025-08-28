package im.ghosty.catboyaddons.features.f7.terms

import cc.polyfrost.oneconfig.events.event.ReceivePacketEvent
import cc.polyfrost.oneconfig.events.event.RenderEvent
import cc.polyfrost.oneconfig.events.event.Stage
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import im.ghosty.catboyaddons.Config
import im.ghosty.catboyaddons.utils.MSTimer
import im.ghosty.catboyaddons.CatboyAddons.mc
import im.ghosty.catboyaddons.utils.MovementHandler
import im.ghosty.catboyaddons.utils.StatusUtils
import im.ghosty.catboyaddons.utils.Utils.scaledResolution
import im.ghosty.catboyaddons.utils.events.InventoryCloseEvent
import net.minecraft.network.play.client.C0EPacketClickWindow
import net.minecraft.network.play.server.S2DPacketOpenWindow

object AutoTerms {

    var firstClick = true
    var timer: MSTimer? = null
    var lastClick: ItemData? = null
    var clicked = false

    @Subscribe
    fun onUpdate(event: RenderEvent) {
        if (event.stage !== Stage.END) return
        if (!Config.autoTerms) return
        if (!TermHandler.inTerminal) return
        if (StatusUtils.dungeonF7Phase != 3) return

        if(TermHandler.termType === TerminalTypes.MELODY) {
            return
        }

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
            clicked = false
            return
        }

        val solution = TermHandler.getSolution() ?: return
        lastClick = (if (!Config.autoTermsReverseOrder) solution.pollFirst() else solution.pollLast()) ?: return
        mc.thePlayer.sendQueue.addToSendQueue(C0EPacketClickWindow(TermHandler.windowId, lastClick!!.slot, if(lastClick!!.rightClick == true) 1 else 0, 0, null, 0))

        firstClick = false
        timer!!.reset()
        clicked = true
    }

    @Subscribe
    fun onRender(event: RenderEvent) {
        if(event.stage !== Stage.END) return
        if(!TermHandler.invWalk) return
        val text1 = "§bIn Terminal: ${TermHandler.termType!!.display}"
        val text2 = "§3Clicks remaining: ${TermHandler.solutionSize - TermHandler.getSolution()!!.size}/${TermHandler.solutionSize}"
        val xSize = mc.fontRendererObj.getStringWidth(text1).coerceAtLeast(mc.fontRendererObj.getStringWidth(text2))
        val posX = ((xSize + scaledResolution.scaledWidth) / 2).toFloat()
        val startY = (scaledResolution.scaledHeight / 2 - 5 - mc.fontRendererObj.FONT_HEIGHT * 2).toFloat()
        mc.fontRendererObj.drawStringWithShadow(text1, posX, startY, 0)
        mc.fontRendererObj.drawStringWithShadow(text2, posX, startY + mc.fontRendererObj.FONT_HEIGHT + 2, 0)
    }

    @Subscribe
    fun onOpenWindow(event: ReceivePacketEvent) {
        if (event.packet !is S2DPacketOpenWindow) return
        clicked = false
    }

    @Subscribe(priority = 10000)
    fun onCloseWindow(event: InventoryCloseEvent) {
        timer = null // that will reset everything eventually
        if(TermHandler.invWalk && TermHandler.termType === TerminalTypes.MELODY) MovementHandler.restore()
    }

}
