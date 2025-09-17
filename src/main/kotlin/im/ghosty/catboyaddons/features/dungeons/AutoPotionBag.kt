package im.ghosty.catboyaddons.features.dungeons

import im.ghosty.catboyaddons.CatboyAddons.mc
import im.ghosty.catboyaddons.Config
import im.ghosty.catboyaddons.utils.Scheduler
import im.ghosty.catboyaddons.utils.StatusUtils
import im.ghosty.catboyaddons.utils.events.WorldChangeEvent

object AutoPotionBag {

    var hasOpenedYet = false

    fun onWorldChange(event: WorldChangeEvent) {
        hasOpenedYet = false
    }

    init {
        Scheduler.loop(10) {
            if(!StatusUtils.inDungeon || hasOpenedYet) return@loop
            if(!Config.autoOpenPotionBag) {
                hasOpenedYet = true
                return@loop
            }
            mc.thePlayer?.sendChatMessage("/potionbag")?.also {
                hasOpenedYet = true
            }
        }
    }

}