package im.ghosty.catboyaddons.features.f7

import cc.polyfrost.oneconfig.events.event.ReceivePacketEvent
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import im.ghosty.catboyaddons.CatboyAddons
import im.ghosty.catboyaddons.Config
import im.ghosty.catboyaddons.utils.LeapHelper
import im.ghosty.catboyaddons.utils.LocUtils
import im.ghosty.catboyaddons.utils.StatusUtils
import im.ghosty.catboyaddons.utils.Utils.removeFormatting
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.play.server.S02PacketChat
import net.minecraft.util.BlockPos

object P3AutoLeap {

    private val regexI4 = Regex("\\[BOSS] Storm: (?:Ouch, that hurt!|Oof)")
    private val regexSSC = Regex("\\[BOSS] Storm: I should have known that I stood no chance\\.")
    private val regexEE = Regex("\\w+ (?:completed|activated) a (?:terminal|lever|device)! \\(7/7\\)")
    private val regexEE3 = Regex("\\w+ (?:completed|activated) a (?:terminal|lever|device)! \\(8/8\\)")
    private val regexTunnel = Regex("The Core entrance is opening!")

    private val locations = mapOf(
        "i4" to Pair(BlockPos(93, 129, 44), BlockPos(90, 133, 46)),
        "ssc" to Pair(BlockPos(106, 119, 92), BlockPos(110, 123, 96)),
        "ee2" to Pair(BlockPos(88, 146, 122), BlockPos(38, 105, 142)),
        "ee3" to Pair(BlockPos(18, 146, 121), BlockPos(-2, 105, 70)),
        "ee4" to Pair(BlockPos(70, 139, 50), BlockPos(38, 105, 30)),
        "tunnel" to Pair(BlockPos(43, 110, 117), BlockPos(65, 138, 55)),
    )

    @Subscribe
    fun onChat(event: ReceivePacketEvent) {
        if (!Config.p3AutoLeap || !StatusUtils.inF7Boss()) return
        if (event.packet !is S02PacketChat) return
        val packet = event.packet as S02PacketChat
        if (packet.type.toInt() != 0) return

        val message = packet.chatComponent.unformattedText.removeFormatting()
        var player: EntityPlayer? = null
        when (StatusUtils.dungeonF7Phase) {
            2 -> {
                if (Config.p3AutoLeapI4 && message.matches(regexI4))
                    player = getPlayerInBox(locations["i4"]!!)
                else if (Config.p3AutoLeapSSC && message.matches(regexSSC))
                    player = getPlayerInBox(locations["ssc"]!!)
            }

            3 -> {
                if (Config.p3AutoLeapEE2 && StatusUtils.dungeonF7SectionP3 == 1 && message.matches(regexEE))
                    player = getPlayerInBox(locations["ee2"]!!)
                else if (Config.p3AutoLeapEE3 && StatusUtils.dungeonF7SectionP3 == 2 && message.matches(regexEE3))
                    player = getPlayerInBox(locations["ee3"]!!)
                else if (Config.p3AutoLeapEE4 && StatusUtils.dungeonF7SectionP3 == 3 && message.matches(regexEE))
                    player = getPlayerInBox(locations["ee4"]!!)
                else if (Config.p3AutoLeapTunnel && StatusUtils.dungeonF7SectionP3 == 4 && message.matches(regexTunnel))
                    player = getPlayerInBox(locations["tunnel"]!!)
            }
        }

        player?.let {
            LeapHelper.leapTo(it)
        }

    }

    fun getPlayerInBox(box: Pair<BlockPos, BlockPos>): EntityPlayer? {
        val world = CatboyAddons.mc.theWorld ?: return null
        val candidates = world.playerEntities
            .filter { LocUtils.isInsideBox(it.position, box.first, box.second) }

        if (candidates.any { it is EntityPlayerSP }) return null;

        return candidates.minByOrNull { p ->
            val dx = p.motionX
            val dy = p.motionY
            val dz = p.motionZ
            dx * dx + dy * dy + dz * dz
        }
    }

}