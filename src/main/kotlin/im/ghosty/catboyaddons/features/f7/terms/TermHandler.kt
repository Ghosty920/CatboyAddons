package im.ghosty.catboyaddons.features.f7.terms

import im.ghosty.catboyaddons.utils.events.PacketReceiveEvent
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import im.ghosty.catboyaddons.Config
import im.ghosty.catboyaddons.utils.Utils.removeFormatting
import im.ghosty.catboyaddons.utils.events.InventoryCloseEvent
import im.ghosty.catboyaddons.utils.events.WorldChangeEvent
import net.minecraft.item.ItemStack
import net.minecraft.network.play.server.S2DPacketOpenWindow
import net.minecraft.network.play.server.S2FPacketSetSlot
import java.util.LinkedList

object TermHandler {

    var inTerminal = false
    var windowId = -1
    var windowTitle = ""
    var termType: TerminalTypes? = null
    var openTime = 0L

    var invWalk = false
    var receivedAll = false
    @JvmField
    var items: ArrayList<ItemData>? = null
    @JvmField
    var solution: LinkedList<ItemData>? = null
    var solutionSize = -1

    @Subscribe(priority = 1000069)
    fun onPacketReceive(event: PacketReceiveEvent) {
        if (event.packet is S2DPacketOpenWindow) {
            val packet = event.packet as S2DPacketOpenWindow

            if(inTerminal) {
                windowId = packet.windowId
                if(invWalk) event.isCancelled = true
                return
            }

            windowTitle = packet.windowTitle.unformattedText.removeFormatting()
            termType = TerminalTypes.entries.find { it.regex.matches(windowTitle) }
            if (termType == null) return

            inTerminal = true
            windowId = packet.windowId
            openTime = System.currentTimeMillis()
            receivedAll = false
            items = ArrayList(termType!!.rows * 9)
            solution = null

            invWalk = !Config.secure && (when(termType!!) {
                TerminalTypes.MELODY -> Config.autoTermsInvWalkMelody
                else -> Config.autoTermsInvWalk
            })
            if (invWalk) event.isCancelled = true

            return
        }

        if (event.packet is S2FPacketSetSlot) {
            if (!inTerminal || receivedAll) return
            val packet = event.packet as S2FPacketSetSlot
            if (packet.func_149175_c() != windowId) return
            if (packet.func_149173_d() == termType!!.rows * 9 - 1) {
                receivedAll = true
                return
            }

            items!!.add(
                ItemData(
                    packet.func_149174_e(),
                    packet.func_149173_d(),
                    false
                )
            )

            if (invWalk) event.isCancelled = true
            return
        }

    }

    fun getSolution(): LinkedList<ItemData>? {
        if (!receivedAll) return null
        if (solution != null) return solution
        if (items == null) return null
        val soluce = when(termType) {
            TerminalTypes.NUMBERS -> NumbersSolver().solve(items!!)
            TerminalTypes.RUBIX -> RubixSolver().solve(items!!)
            TerminalTypes.REDGREEN -> RedGreenSolver().solve(items!!)
            TerminalTypes.STARTSWITH -> {
                val prefix = TerminalTypes.STARTSWITH.regex.find(windowTitle)!!.groupValues[1]
                StartsWithSolver(prefix).solve(items!!)
            }
            TerminalTypes.COLORS -> {
                val color = TerminalTypes.COLORS.regex.find(windowTitle)!!.groupValues[1]
                ColorsSolver(color).solve(items!!)
            }
            else -> null
        }
        if(soluce != null) solution = LinkedList(soluce)
        solutionSize = soluce?.size ?: -1
        return solution
    }

    @Subscribe
    fun onInventoryClose(event: InventoryCloseEvent) {
        reset()
    }

    @Subscribe
    fun onWorldChange(event: WorldChangeEvent) {
        reset()
    }

    fun reset() {
        inTerminal = false
        windowId = -1
        windowTitle = ""
        termType = null
        openTime = 0L
        invWalk = false
        receivedAll = false
        items = null
        solution = null
        solutionSize = -1
    }

}

data class ItemData(val item: ItemStack, val slot: Int, var rightClick: Boolean?) {
    fun cloneWithRC(): ItemData = ItemData(item, slot, true)
}

enum class TerminalTypes(val regex: Regex, val rows: Int, val display: String) {
    NUMBERS(Regex("^Click in order!$"), 4, "§dNumbers"),
    RUBIX(Regex("^Change all to same color!$"), 5, "§9Rubix"),
    REDGREEN(Regex("^Correct all the panes!$"), 5, "§cRed §aGreen"),
    STARTSWITH(Regex("^What starts with: '(.+?)'\\?$"), 5, "§aStarts with"),
    COLORS(Regex("^Select all the (.+?) items!$"), 6, "§eColors"),
    MELODY(Regex("^Click the button on time!$"), 6, "§6Melody")
}