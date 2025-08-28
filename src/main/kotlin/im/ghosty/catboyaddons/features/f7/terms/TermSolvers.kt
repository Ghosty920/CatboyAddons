package im.ghosty.catboyaddons.features.f7.terms

import im.ghosty.catboyaddons.utils.Utils.removeFormatting
import net.minecraft.item.Item
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
        val items = itemList.filter { it.item.metadata != 15 && Item.getIdFromItem(it.item.item) == 160 }
        var bestIndex = -1
        var minTotal = 1000

        for (targetIndex in 0..4) {
            var totalClicks = 0
            for (i in 0..<items.size) {
                val currIndex = order.indexOf(items[i].item.metadata)
                val leftClicks = (targetIndex - currIndex + order.size) % order.size
                val rightClicks = (currIndex - targetIndex + order.size) % order.size
                totalClicks += min(leftClicks, rightClicks)
            }
            if(totalClicks < minTotal) {
                minTotal = totalClicks
                bestIndex = targetIndex
            }
        }

        for(i in 0..<items.size) {
            val item = items[i]
            val currIndex = order.indexOf(item.item.metadata)
            val leftClicks = (bestIndex - currIndex + order.size) % order.size
            val rightClicks = (currIndex - bestIndex + order.size) % order.size

            if(leftClicks <= rightClicks) {
                for(j in 1..leftClicks)
                    solution.add(item)
            } else {
                for(j in 1..leftClicks)
                    solution.add(item.cloneWithRC())
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
        return setClickOrder(itemList.filter { !it.item.isItemEnchanted && it.item.displayName.startsWith(prefix, true) })
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
                return@filter name.startsWith(color)
            }
        )
    }

}