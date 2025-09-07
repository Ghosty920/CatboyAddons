package im.ghosty.catboyaddons.features.f7.terms

import im.ghosty.catboyaddons.CatboyAddons.mc
import im.ghosty.catboyaddons.Config
import im.ghosty.catboyaddons.utils.StatusUtils
import im.ghosty.catboyaddons.utils.Utils.expandBy
import im.ghosty.catboyaddons.utils.Utils.removeFormatting
import im.ghosty.catboyaddons.utils.render.Render3DUtils
import im.ghosty.catboyaddons.utils.render.Render3DUtils.getRenderBox
import im.ghosty.catboyaddons.utils.render.Render3DUtils.renderBox
import net.minecraft.entity.item.EntityArmorStand
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object TerminalESP {

    val inactiveTitles = arrayOf("Inactive Terminal", "Inactive Device", "Not Activated")

    @SubscribeEvent
    fun onRender(event: RenderWorldLastEvent) {
        if (!Config.terminalESP) return
        if (StatusUtils.dungeonF7Phase != 3) return
        if(mc.thePlayer == null || mc.theWorld == null) return
        Render3DUtils.enableSmooth = true
        mc.theWorld.loadedEntityList
            .filterIsInstance<EntityArmorStand>()
            .filter { inactiveTitles.contains(it.name.removeFormatting()) }
            .filter { it.getDistanceSqToEntity(mc.thePlayer) > 5 }
            .forEach {
                val aabb = it.getRenderBox().expandBy(0.2, 1.0, 0.0)
                renderBox(aabb, Config.terminalESPLineWidth, Config.terminalESPOutlineColor.rgb)
            }
        Render3DUtils.enableSmooth = false
    }

}