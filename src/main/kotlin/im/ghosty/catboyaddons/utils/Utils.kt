package im.ghosty.catboyaddons.utils

import im.ghosty.catboyaddons.CatboyAddons
import im.ghosty.catboyaddons.CatboyAddons.mc
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting
import org.apache.logging.log4j.LogManager

object Utils {

    val scaledResolution = ScaledResolution(mc)

    fun String.remove(vararg patterns: String): String = patterns.fold(this) { acc, s -> acc.replace(s, "") }
    fun String.remove(vararg patterns: Regex): String = patterns.fold(this) { acc, r -> acc.replace(r, "") }

    private val unicodeRegex = Regex("[^\\u0000-\\u007F§]")
    fun String.removeUnicode() = this.remove(unicodeRegex)

    fun String.removeFormatting(): String = EnumChatFormatting.getTextWithoutFormattingCodes(this)

    val logger = LogManager.getLogger("CatboyAddons")

    fun sendToChat(message: String) = mc.ingameGUI?.chatGUI?.printChatMessage(ChatComponentText(CatboyAddons.PREFIX + message))
    fun sendToConsole(message: String) = println("[Catboy] $message")

    fun <E> ArrayList<E>.shift(): E = this.removeAt(0)
    fun <E> ArrayList<E>.pop(): E = this.removeAt(this.size - 1)

    fun AxisAlignedBB.expandBy(offXZ: Double, offMinY: Double, offMaxY: Double): AxisAlignedBB {
        return AxisAlignedBB(
            minX - offXZ,
            minY + offMinY,
            minZ - offXZ,
            maxX + offXZ,
            maxY + offMaxY,
            maxZ + offXZ
        )
    }

}