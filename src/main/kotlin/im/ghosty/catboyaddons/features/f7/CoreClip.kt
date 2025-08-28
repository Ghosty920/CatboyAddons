package im.ghosty.catboyaddons.features.f7

import cc.polyfrost.oneconfig.events.event.Stage
import cc.polyfrost.oneconfig.events.event.TickEvent
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import im.ghosty.catboyaddons.CatboyAddons.mc
import im.ghosty.catboyaddons.Config
import im.ghosty.catboyaddons.utils.LocUtils
import im.ghosty.catboyaddons.utils.MovementHandler
import im.ghosty.catboyaddons.utils.StatusUtils

object CoreClip {

    @Subscribe
    fun onTick(event: TickEvent) {
        if (event.stage !== Stage.START) return
        if (Config.secure || !Config.coreClip) return
        if (!StatusUtils.inF7Boss()) return

        val loc = LocUtils.getPlayerLoc() ?: return
        if(loc.yCoord != 115.0) return
        if(loc.xCoord !in 52.0..57.0) return

        val distance = loc.zCoord - 54.5
        if(distance in 0.8..0.80001) {
            MovementHandler.stop()
            mc.thePlayer.setPosition(loc.xCoord, loc.yCoord, 55.2376)
            mc.addScheduledTask {
                MovementHandler.restore()
                mc.thePlayer.setPosition(loc.xCoord, loc.yCoord, 53.699)
            }
        } else if(distance in -0.80001..-0.8) {
            MovementHandler.stop()
            mc.thePlayer.setPosition(loc.xCoord, loc.yCoord, 53.7624)
            mc.addScheduledTask {
                MovementHandler.restore()
                mc.thePlayer.setPosition(loc.xCoord, loc.yCoord, 55.301)
            }
        }
    }

}