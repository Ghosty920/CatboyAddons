package im.ghosty.catboyaddons.utils

import im.ghosty.catboyaddons.CatboyAddons.mc
import net.minecraft.util.BlockPos
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent

object StatusUtils {

    var onHypixel = false
    var inSkyblock = false
    var inDungeon = false
    var dungeonFloor: String? = null
    var dungeonFloorNumber: Int? = null
    @JvmStatic
    val dungeonIsMM get() = dungeonFloor?.startsWith("M") ?: false
    var dungeonInBoss = false
    var dungeonF7Phase: Int? = null
    var dungeonF7SectionP3: Int? = null

    fun inF7Boss() = onHypixel && inSkyblock && inDungeon && dungeonFloorNumber == 7 && dungeonInBoss

    private var tickTimer = 0

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if(event.phase != TickEvent.Phase.START) return
        if ((tickTimer++) != 10) return
        tickTimer = 0

        inSkyblock = onHypixel && mc.theWorld?.scoreboard?.getObjectiveInDisplaySlot(1)?.name == "SBScoreboard"
        if (inSkyblock) {
            dungeonInBoss = isInBossRoom()
            dungeonF7Phase = getPhase()
            dungeonF7SectionP3 = getP3Section()
        } else {
            inDungeon = false
            dungeonFloor = null
            dungeonFloorNumber = null
            dungeonInBoss = false
            dungeonF7Phase = null
            dungeonF7SectionP3 = null
        }

        if (!inDungeon) updateDungeonStatus()
    }

    private fun updateDungeonStatus() = ScoreboardUtils.sidebarLines.find {
        ScoreboardUtils.cleanSB(it).run {
            contains("The Catacombs (") && ! contains("Queue")
        }
    }?.run {
        inDungeon = true
        dungeonFloor = ScoreboardUtils.cleanSB(this).substringAfter("(").substringBefore(")")
        dungeonFloorNumber = dungeonFloor?.lastOrNull()?.digitToIntOrNull() ?: 0
    }

    private val bossRoomCorners = mapOf(
        7 to Pair(BlockPos(- 8, 0, - 8), BlockPos(134, 254, 147)),
        6 to Pair(BlockPos(- 40, 51, - 8), BlockPos(22, 110, 134)),
        5 to Pair(BlockPos(- 40, 112, - 8), BlockPos(50, 53, 118)),
        4 to Pair(BlockPos(- 40, 112, - 40), BlockPos(50, 53, 47)),
        3 to Pair(BlockPos(- 40, 118, - 40), BlockPos(42, 64, 31)),
        2 to Pair(BlockPos(- 40, 99, - 40), BlockPos(24, 54, 59)),
        1 to Pair(BlockPos(- 14, 55, 49), BlockPos(- 72, 146, - 40))
    )

    private fun isInBossRoom(): Boolean {
        val playerPos = mc.thePlayer.position ?: return false
        val floor = dungeonFloorNumber ?: return false
        val corners = bossRoomCorners[floor] ?: return false
        return LocUtils.isInsideBox(playerPos, corners.first, corners.second)
    }

    private fun getPhase(): Int? {
        if (dungeonFloorNumber != 7 || !dungeonInBoss) return null
        val playerPosition = mc.thePlayer.position ?: return null

        return when {
            playerPosition.y > 210 -> 1
            playerPosition.y > 155 -> 2
            playerPosition.y > 100 -> 3
            playerPosition.y > 45 -> 4
            else -> 5
        }
    }

    private val P3Sections = listOf(
        Pair(BlockPos(90, 158, 123), BlockPos(111, 105, 32)),  //  1
        Pair(BlockPos(16, 158, 122), BlockPos(111, 105, 143)), //  2
        Pair(BlockPos(19, 158, 48), BlockPos(- 3, 106, 142)),  //  3
        Pair(BlockPos(91, 158, 50), BlockPos(- 3, 106, 30))    //  4
    )

    private fun getP3Section(): Int? {
        if (dungeonF7Phase != 3) return null
        val playerPos = mc.thePlayer.position ?: return null

        P3Sections.forEachIndexed { i, (a, b) ->
            if (LocUtils.isInsideBox(playerPos, a, b)) {
                return i + 1
            }
        }

        return null
    }



    @SubscribeEvent
    fun onServerConnect(event: ClientConnectedToServerEvent) {
        onHypixel = mc.currentServerData?.serverIP?.contains("hypixel.net") ?: false
    }

    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Load) {
        inSkyblock = false
        inDungeon = false
        dungeonFloor = null
        dungeonInBoss = false
        dungeonF7Phase = null
        dungeonF7SectionP3 = null
    }

    @SubscribeEvent
    fun onServerDisconnect(event: ClientDisconnectionFromServerEvent) {
        onHypixel = false
        inDungeon = false
        dungeonFloor = null
        dungeonInBoss = false
        dungeonF7Phase = null
        dungeonF7SectionP3 = null
    }

}