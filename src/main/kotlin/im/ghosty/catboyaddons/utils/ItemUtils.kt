package im.ghosty.catboyaddons.utils

import im.ghosty.catboyaddons.CatboyAddons.mc
import im.ghosty.catboyaddons.utils.Utils.removeFormatting
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

    fun getHotbarItemByName(name: String, color: Boolean = false): ItemStack? {
        for(i in 0..8) {
            val item = mc.thePlayer?.inventoryContainer?.getSlot(i + 36)?.stack ?: continue
            val itemName = item.displayName.run {
                if(!color) this.removeFormatting()
                else this
            }
            if(itemName == name)
                return item
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
