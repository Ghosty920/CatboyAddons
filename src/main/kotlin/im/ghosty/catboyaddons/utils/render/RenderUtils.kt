package im.ghosty.catboyaddons.utils.render

import cc.polyfrost.oneconfig.utils.dsl.getAlpha
import cc.polyfrost.oneconfig.utils.dsl.getBlue
import cc.polyfrost.oneconfig.utils.dsl.getGreen
import cc.polyfrost.oneconfig.utils.dsl.getRed
import im.ghosty.catboyaddons.CatboyAddons.mc
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.opengl.GL11.*

object RenderUtils {

    val renderManager = mc.renderManager

    var partialTicks = 0f

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onRenderWorld(event: RenderWorldLastEvent) {
        this.partialTicks = event.partialTicks
    }

    fun color(color: Int) {
        glColor4d(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0, color.getAlpha() / 255.0)
    }

    fun smooth(state: Boolean) {
        if (state) {
            glEnable(GL_LINE_SMOOTH)
            glEnable(GL_POLYGON_SMOOTH)
            glEnable(GL_POINT_SMOOTH)
            glEnable(GL_BLEND)
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
            glHint(GL_LINE_SMOOTH_HINT, GL_NICEST)
            glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST)
            glHint(GL_POINT_SMOOTH_HINT, GL_NICEST)
        } else {
            glDisable(GL_LINE_SMOOTH)
            glDisable(GL_POLYGON_SMOOTH)
            glEnable(GL_POINT_SMOOTH)
        }
    }

}