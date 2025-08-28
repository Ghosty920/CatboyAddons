package im.ghosty.catboyaddons.features.f7.terms

import cc.polyfrost.oneconfig.events.event.ReceivePacketEvent
import cc.polyfrost.oneconfig.events.event.WorldLoadEvent
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import im.ghosty.catboyaddons.utils.Utils.removeFormatting
import im.ghosty.catboyaddons.utils.events.InventoryCloseEvent
import net.minecraft.item.ItemStack
import net.minecraft.network.play.server.S2DPacketOpenWindow
import net.minecraft.network.play.server.S2FPacketSetSlot
import java.util.LinkedList

object TermHandler {

    val cancelEvents = false

    var inTerminal = false
    var windowId = -1
    var windowTitle = ""
    var termType: TerminalTypes? = null
    var openTime = 0L

    var receivedAll = false
    var items: ArrayList<ItemData>? = null
    var solution: LinkedList<ItemData>? = null

    @Subscribe(priority = 1000069)
    fun onPacketReceive(event: ReceivePacketEvent) {
        if (event.packet is S2DPacketOpenWindow) {
            val packet = event.packet as S2DPacketOpenWindow;
            windowTitle = packet.windowTitle.unformattedText.removeFormatting()
            termType = TerminalTypes.entries.find { it.regex.matches(windowTitle) }

            if (termType == null) {
                reset()
                return
            }

            inTerminal = true
            windowId = packet.windowId
            openTime = System.currentTimeMillis()
            receivedAll = false
            items = ArrayList(termType!!.rows * 9)
            solution = null

            if (cancelEvents) event.isCancelled = true
            return
        }

        if (event.packet is S2FPacketSetSlot) {
            if (!inTerminal) return
            val packet = event.packet as S2FPacketSetSlot
            if (packet.func_149175_c() != windowId) return
            if (packet.func_149173_d() > termType!!.rows * 9 - 1) {
                receivedAll = true
                return
            }

            items!!.add(
                ItemData(
                    packet.func_149174_e(),
                    packet.func_149173_d(),
                    packet.func_149175_c(),
                    false
                )
            )

            if (cancelEvents) event.isCancelled = true
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
        return solution
    }

    @Subscribe
    fun onInventoryClose(event: InventoryCloseEvent) {
        reset()
    }

    @Subscribe
    fun onWorldLoad(event: WorldLoadEvent) {
        reset()
    }

    fun reset() {
        inTerminal = false
        windowId = -1
        windowTitle = ""
        termType = null
        openTime = 0L
        receivedAll = false
        items = null
        solution = null
    }

}

class ItemData(val item: ItemStack, val slot: Int, val windowId: Int, var rightClick: Boolean?) {
    fun cloneWithRC(): ItemData = ItemData(item, slot, windowId, true)
}

enum class TerminalTypes(val regex: Regex, val rows: Int) {
    NUMBERS(Regex("^Click in order!$"), 4),
    RUBIX(Regex("^Change all to same color!$"), 5),
    REDGREEN(Regex("^Correct all the panes!$"), 5),
    STARTSWITH(Regex("^What starts with: '(.+?)'\\?$"), 5),
    COLORS(Regex("^Select all the (.+?) items!$"), 6),
    MELODY(Regex("^Click the button on time!$"), 5) // TODO implement melody solver
}