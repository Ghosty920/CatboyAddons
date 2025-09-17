package im.ghosty.catboyaddons.features.f7.terms

import cc.polyfrost.oneconfig.events.event.HudRenderEvent
import im.ghosty.catboyaddons.utils.events.PacketReceiveEvent
import cc.polyfrost.oneconfig.events.event.RenderEvent
import cc.polyfrost.oneconfig.events.event.Stage
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import im.ghosty.catboyaddons.CatboyAddons.mc
import im.ghosty.catboyaddons.Config
import im.ghosty.catboyaddons.utils.MSTimer
import im.ghosty.catboyaddons.utils.MovementHandler
import im.ghosty.catboyaddons.utils.Scheduler
import im.ghosty.catboyaddons.utils.StatusUtils
import im.ghosty.catboyaddons.utils.Utils.scaledResolution
import im.ghosty.catboyaddons.utils.events.InventoryCloseEvent
import im.ghosty.catboyaddons.utils.events.PacketSendEvent
import net.minecraft.item.Item
import net.minecraft.network.play.client.C0EPacketClickWindow
import net.minecraft.network.play.server.S2DPacketOpenWindow
import net.minecraft.network.play.server.S2FPacketSetSlot
import kotlin.math.floor

object AutoTerms {

    var firstClick = true
    var timer: MSTimer? = null
    var lastClick: ItemData? = null
    var clicked = false

    var melodyStep = 0
    var melodyCorrect = -1
    var melodyInvWalkTimer = MSTimer(0)

    /**
     * Main solver
     */
    @Subscribe
    fun onUpdate(event: RenderEvent) {
        if (event.stage !== Stage.END) return
        if (!Config.autoTerms) return
        if (!TermHandler.inTerminal) return
        if (StatusUtils.dungeonF7Phase != 3) return
        if (TermHandler.termType === TerminalTypes.MELODY) return

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

            if (!Config.autoTermsReverseOrder)
                TermHandler.getSolution()?.addFirst(lastClick!!)
            else TermHandler.getSolution()?.addLast(lastClick!!)

            lastClick = null
            clicked = false
            return
        }

        val solution = TermHandler.getSolution() ?: return
        lastClick = (if (!Config.autoTermsReverseOrder || TermHandler.termType === TerminalTypes.NUMBERS)
            solution.pollFirst()
        else solution.pollLast()) ?: return
        click(lastClick!!.slot, lastClick!!.rightClick)

        firstClick = false
        timer!!.reset()
        clicked = true
    }

    /**
     * Melody solver
     */
    @Subscribe
    fun onSlotAdd(event: PacketReceiveEvent) {
        if (!Config.autoTerms) return
        if (!TermHandler.inTerminal) return
        if (StatusUtils.dungeonF7Phase != 3) return
        if (TermHandler.termType !== TerminalTypes.MELODY) return
        val packet = event.packet as? S2FPacketSetSlot ?: return
        if (packet.func_149175_c() != TermHandler.windowId) return

        val slot = packet.func_149173_d()
        val item = packet.func_149174_e()
        if (Item.getIdFromItem(item.item) != 160) return

        // item to click / "correct"
        if (item.metadata == 2) {
            melodyCorrect = slot - 1
            return
        }

        // the current item that goes left/right
        if (item.metadata == 5) {
            melodyStep = floor(slot / 9.0f).toInt() - 1
            val slotMod = (slot % 9) - 1
            if (slotMod != melodyCorrect) return
            val button = melodyStep * 9 + 16
            println("Melody -> Clicking $button")
            Scheduler.add {
                click(button, false)
            }
        }
    }

    /**
     * Melody safe delay
     */
    @Subscribe
    fun onMelodyClick(event: PacketSendEvent) {
        if (!Config.autoTerms) return
        if (!TermHandler.inTerminal || !TermHandler.invWalk) return
        if (StatusUtils.dungeonF7Phase != 3) return
        if (TermHandler.termType !== TerminalTypes.MELODY) return
        val packet = event.packet as? C0EPacketClickWindow ?: return
        if (packet.windowId != TermHandler.windowId) return

        MovementHandler.stopAll()
        melodyInvWalkTimer.reset()
        Scheduler.addMS(Config.autoTermsInvWalkMelodySafe) {
            MovementHandler.restoreAll()
        }
    }

    /**
     * Render
     */
    @Subscribe
    fun onRender(event: HudRenderEvent) {
        if (!Config.autoTerms) return
        if (StatusUtils.dungeonF7Phase != 3) return
        if (!TermHandler.invWalk || TermHandler.solutionSize < 0) return

        val solutionSize = if (TermHandler.termType !== TerminalTypes.MELODY) TermHandler.solutionSize else 4
        val clicksRemaining =
            solutionSize - (if (TermHandler.termType !== TerminalTypes.MELODY) TermHandler.getSolution()!!.size else melodyStep)

        var text1 = "§bIn Terminal: ${TermHandler.termType!!.display}"
        val text2 =
            "§3Clicks remaining: ${clicksRemaining}/${solutionSize}"
        val xSize = mc.fontRendererObj.getStringWidth(text1).coerceAtLeast(mc.fontRendererObj.getStringWidth(text2))
        val posX = ((scaledResolution.scaledWidth - xSize) / 2).toFloat()

        if(!melodyInvWalkTimer.hasPassed(Config.autoTermsInvWalkMelodySafe.toLong())) {
            text1 += " §4(${melodyInvWalkTimer.timeLeft(Config.autoTermsInvWalkMelodySafe.toLong())} ms)"
        }

        val startY = (scaledResolution.scaledHeight / 2 - 5 - mc.fontRendererObj.FONT_HEIGHT * 2).toFloat()
        mc.fontRendererObj.drawStringWithShadow(text1, posX, startY, 0)
        mc.fontRendererObj.drawStringWithShadow(text2, posX, startY + mc.fontRendererObj.FONT_HEIGHT + 2, 0)
    }

    @Subscribe
    fun onOpenWindow(event: PacketReceiveEvent) {
        if (event.packet !is S2DPacketOpenWindow) return
        clicked = false
        melodyCorrect = -1
    }

    @Subscribe(priority = 10000)
    fun onCloseWindow(event: InventoryCloseEvent) {
        timer = null // that will reset everything eventually
    }

    private fun click(slot: Int, rc: Boolean?) {
        mc.thePlayer.sendQueue.addToSendQueue(
            C0EPacketClickWindow(
                TermHandler.windowId,
                slot,
                if (rc == true) 1 else 0,
                0,
                null,
                0
            )
        )
    }

}
