package im.ghosty.catboyaddons.utils.render

import cc.polyfrost.oneconfig.utils.dsl.getAlpha
import im.ghosty.catboyaddons.utils.render.RenderUtils.partialTicks
import im.ghosty.catboyaddons.utils.render.RenderUtils.renderManager
import net.minecraft.client.renderer.GlStateManager.*
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.WorldRenderer
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.Entity
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.Vec3
import org.lwjgl.opengl.GL11.*

object Render3DUtils {

    var enableSmooth = false

    private val tessellator: Tessellator = Tessellator.getInstance()
    private val worldRenderer: WorldRenderer = tessellator.worldRenderer

    inline val Entity.renderX: Double
        get() = prevPosX + (posX - prevPosX) * partialTicks
    inline val Entity.renderY: Double
        get() = prevPosY + (posY - prevPosY) * partialTicks
    inline val Entity.renderZ: Double
        get() = prevPosZ + (posZ - prevPosZ) * partialTicks
    inline val Entity.renderVec: Vec3
        get() = Vec3(renderX, renderY, renderZ)

    fun Entity.getRenderBox(): AxisAlignedBB {
        val x = renderX - renderManager.viewerPosX
        val y = renderY - renderManager.viewerPosY
        val z = renderZ - renderManager.viewerPosZ
        return entityBoundingBox.run {
            return@run AxisAlignedBB(
                minX - posX + x,
                minY - posY + y,
                minZ - posZ + z,
                maxX - posX + x,
                maxY - posY + y,
                maxZ - posZ + z
            )
        }
    }

    fun renderEntityBox(
        entity: Entity,
        lineWidth: Float,
        outlineColor: Int
    ) {
        renderBox(entity.getRenderBox(), lineWidth, outlineColor)
    }

    fun renderBox(
        aabb: AxisAlignedBB,
        lineWidth: Float,
        outlineColor: Int
    ) {
        glPushMatrix()
        glPushAttrib(GL_ALL_ATTRIB_BITS)
        disableBlend()
        disableTexture2D()
        blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        disableDepth()
        if (enableSmooth) RenderUtils.smooth(true)

        if (outlineColor.getAlpha() > 0) {
            glLineWidth(lineWidth)
            RenderUtils.color(outlineColor)
            drawOutlinedBox(aabb)
        }

        if (enableSmooth) RenderUtils.smooth(false)
        enableDepth()
        enableTexture2D()
        enableBlend()
        glPopAttrib()
        glPopMatrix()
    }

    private fun drawOutlinedBox(aabb: AxisAlignedBB) {
        worldRenderer.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION)

        worldRenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex()

        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex()

        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex()

        tessellator.draw()
    }

}