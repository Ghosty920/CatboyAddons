package im.ghosty.catboyaddons.commands

import cc.polyfrost.oneconfig.utils.commands.annotations.Command
import cc.polyfrost.oneconfig.utils.commands.annotations.Main
import cc.polyfrost.oneconfig.utils.commands.annotations.SubCommand
import im.ghosty.catboyaddons.CatboyAddons
import im.ghosty.catboyaddons.CatboyAddons.mc
import im.ghosty.catboyaddons.Config
import im.ghosty.catboyaddons.utils.LeapHelper
import im.ghosty.catboyaddons.utils.StatusUtils
import im.ghosty.catboyaddons.utils.Utils
import im.ghosty.catboyaddons.utils.Utils.removeFormatting
import net.minecraft.util.ChatComponentText

@Command(value = CatboyAddons.MODID, aliases = ["catboy", "cba"])
object MainCommand {

    @Main(description = "Open the config")
    fun handle() {
        Config.openGui()
    }

    @SubCommand(description = "Debug! :3", aliases = ["dev"])
    fun debug() {
        mc.ingameGUI?.chatGUI?.printChatMessage(
            ChatComponentText(
                """
                ${CatboyAddons.PREFIX}§e§lDEBUGGING!
                §${if (StatusUtils.onHypixel) "a" else "c"}Hypixel §${if (StatusUtils.inSkyblock) "a" else "c"}Skyblock §${if (StatusUtils.inDungeon) "a" else "c"}Dungeons
                §f${StatusUtils.dungeonFloor ?: "NO-FLOOR"} ${if (StatusUtils.dungeonInBoss) "Boss" else "Clear"} ${StatusUtils.dungeonF7Phase ?: "NO-PHASE"} ${StatusUtils.dungeonF7SectionP3 ?: "NO-P3"}
            """.trimIndent()
            )
        )
    }

    @SubCommand(description = "Leap to a player", aliases = ["leapto"])
    fun leap(target: String) {
        /*mc.theWorld?.playerEntities?.find { it.name.equals(target, true) }?.let {
            Utils.sendToChat("§aAttempting to leap to §f${it.displayName.formattedText}")
            LeapHelper.leapTo(it)
        }*/
        LeapHelper.leapTo(target)
    }

}