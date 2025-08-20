package im.ghosty.catboyaddons.utils

import cc.polyfrost.oneconfig.events.event.ReceivePacketEvent
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import im.ghosty.catboyaddons.CatboyAddons.mc
import im.ghosty.catboyaddons.utils.Utils.removeFormatting
import im.ghosty.catboyaddons.utils.Utils.removeUnicode
import net.minecraft.network.play.server.S3CPacketUpdateScore
import net.minecraft.network.play.server.S3DPacketDisplayScoreboard
import net.minecraft.network.play.server.S3EPacketTeams
import net.minecraft.scoreboard.ScorePlayerTeam
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object ScoreboardUtils {

    var sidebarLines: List<String> = emptyList()
        private set

    @Subscribe
    fun onScoreboardChange(event: ReceivePacketEvent) {
        if (event.packet !is S3EPacketTeams
            && event.packet !is S3CPacketUpdateScore
            && event.packet !is S3DPacketDisplayScoreboard
        ) return

        getSidebarLines()
    }

    @SubscribeEvent
    fun onWorldUnload(event: WorldEvent.Unload) {
        sidebarLines = emptyList()
    }


    private fun getSidebarLines() {
        val objective = mc.theWorld?.scoreboard?.getObjectiveInDisplaySlot(1)
        if (objective == null) {
            sidebarLines = emptyList()
            return
        }

        sidebarLines = mc.theWorld.scoreboard.getSortedScores(objective).asSequence()
            .filterNot { it?.playerName?.startsWith("#") == true }.take(15)
            .map {
                ScorePlayerTeam.formatPlayerName(
                    mc.theWorld.scoreboard.getPlayersTeam(it.playerName),
                    it.playerName
                )
            }
            .plus(objective.displayName).toList()
    }

    fun cleanSB(scoreboard: String): String = scoreboard.removeFormatting().removeUnicode()

}