package im.ghosty.catboyaddons.features.f7.terms

import im.ghosty.catboyaddons.utils.Utils.removeFormatting
import net.minecraft.item.Item
import kotlin.math.abs
import kotlin.math.min

interface TermSolver {

    fun solve(itemList: ArrayList<ItemData>): List<ItemData>

}

class NumbersSolver : TermSolver {

    override fun solve(itemList: ArrayList<ItemData>): List<ItemData> {
        return itemList.filter { it.item.metadata == 14 }
            .sortedBy { it.item.stackSize }
    }

}

class RubixSolver : TermSolver {

    private val order = intArrayOf(14, 1, 4, 13, 11);

    override fun solve(itemList: ArrayList<ItemData>): List<ItemData> {
        val solution = arrayListOf<ItemData>()
        val items = setClickOrder(itemList.filter { it.item.metadata != 15 && Item.getIdFromItem(it.item.item) == 160 })
        var bestIndex = -1
        var bestClicks = 1000

        for (targetIndex in 0..4) {
            var totalClicks = 0
            for (i in 0..<items.size) {
                val currIndex = order.indexOf(items[i].item.metadata)
                if(currIndex == targetIndex) continue
                val clicks = (bestIndex - currIndex + order.size) % order.size
                totalClicks += min(clicks, (5 - clicks) % order.size)
            }
            if(totalClicks < bestClicks) {
                bestClicks = totalClicks
                bestIndex = targetIndex
            }
        }

        for(i in 0..<items.size) {
            val item = items[i]
            val currIndex = order.indexOf(item.item.metadata)

            val leftClicks = (bestIndex - currIndex + order.size) % order.size
            val rightClicks = (5 - leftClicks) % order.size

            if(leftClicks <= rightClicks) {
                for(j in 1..leftClicks)
                    solution.add(item)
            } else {
                val itemRC = item.cloneWithRC()
                for(j in 1..rightClicks)
                    solution.add(itemRC)
            }
        }

        return solution
    }

}

class RedGreenSolver : TermSolver {

    override fun solve(itemList: ArrayList<ItemData>): List<ItemData> {
        return setClickOrder(itemList.filter { it.item.metadata == 14 })
    }

}

class StartsWithSolver(val prefix: String) : TermSolver {

    override fun solve(itemList: ArrayList<ItemData>): List<ItemData> {
        return setClickOrder(itemList.filter { !it.item.isItemEnchanted && it.item.displayName.removeFormatting().startsWith(prefix, true) })
    }

}

class ColorsSolver(val color: String) : TermSolver {

    val colorReplacements = mapOf(
        "light gray" to "silver",
        "wool" to "white",
        "bone" to "white",
        "ink" to "black",
        "lapis" to "blue",
        "cocoa" to "brown",
        "dandelion" to "yellow",
        "rose" to "red",
        "cactus" to "green"
    )

    override fun solve(itemList: ArrayList<ItemData>): List<ItemData> {
        return setClickOrder(
            itemList.filter {
                if (it.item.isItemEnchanted) return@filter false
                var name = it.item.displayName.removeFormatting().lowercase()
                colorReplacements.forEach { (from, to) -> name = name.replace(Regex("^$from"), to) }
                return@filter name.startsWith(color, true)
            }
        )
    }

}