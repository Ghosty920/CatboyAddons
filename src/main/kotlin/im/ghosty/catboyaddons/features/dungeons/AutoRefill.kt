package im.ghosty.catboyaddons.features.dungeons

import im.ghosty.catboyaddons.CatboyAddons.mc
import im.ghosty.catboyaddons.Config
import im.ghosty.catboyaddons.utils.ItemUtils.getHotbarItemByName
import im.ghosty.catboyaddons.utils.Scheduler
import im.ghosty.catboyaddons.utils.StatusUtils

object AutoRefill {

    init {
        Scheduler.loop(5) {
            if (Config.autoRefillPearls) {
                if (!StatusUtils.inDungeon) return@loop
                val item = getHotbarItemByName("§fEnder Pearl", true) ?: return@loop
                val missing = 16 - item.stackSize
                if(missing >= 8)
                    mc.thePlayer?.sendChatMessage("/gfs ender_pearl $missing")
            }
            if (Config.autoRefillJerries) {
                if (!StatusUtils.inDungeon && !StatusUtils.inKuudra) return@loop
                val item = getHotbarItemByName("§fInflatable Jerry", true) ?: return@loop
                val missing = 64 - item.stackSize
                if(missing >= 32)
                    mc.thePlayer?.sendChatMessage("/gfs ender_pearl $missing")
            }
        }
    }

}