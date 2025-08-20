package im.ghosty.catboyaddons.utils

import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3

object LocUtils {

    fun isInsideBox(pos: Vec3, a: Vec3, b: Vec3): Boolean {
        val minX = minOf(a.xCoord, b.xCoord)
        val minY = minOf(a.yCoord, b.yCoord)
        val minZ = minOf(a.zCoord, b.zCoord)
        val maxX = maxOf(a.xCoord, b.xCoord)
        val maxY = maxOf(a.yCoord, b.yCoord)
        val maxZ = maxOf(a.zCoord, b.zCoord)

        val x = pos.xCoord in minX .. maxX
        val y = pos.yCoord in minY .. maxY
        val z = pos.zCoord in minZ .. maxZ
        return x && y && z
    }

    fun isInsideBox(pos: BlockPos, a: BlockPos, b: BlockPos) = isInsideBox(pos.toVec(), a.toVec(), b.toVec())

    fun BlockPos.toVec() = Vec3(this.x.toDouble(), this.y.toDouble(), this.z.toDouble())

}
