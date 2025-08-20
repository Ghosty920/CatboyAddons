package im.ghosty.catboyaddons.utils

import im.ghosty.catboyaddons.CatboyAddons.mc
import net.minecraft.item.ItemStack

object ItemUtils {

    fun getHotbarSlotByItemId(id: String): Int? {
        for (i in 0..8) {
            val item = mc.thePlayer?.inventoryContainer?.getSlot(i + 36)?.stack ?: continue
            if (getItemId(item) == id)
                return i
        }
        return null
    }

    fun getHeldItemId(): String? {
        val item = mc.thePlayer?.currentEquippedItem
        return getItemId(item)
    }

    fun getItemId(item: ItemStack?): String? {
        return item
            ?.tagCompound
            ?.getCompoundTag("ExtraAttributes")
            ?.getString("id")
            ?.takeIf { it.isNotEmpty() }
    }

}
