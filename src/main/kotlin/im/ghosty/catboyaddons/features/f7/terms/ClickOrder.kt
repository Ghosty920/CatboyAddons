package im.ghosty.catboyaddons.features.f7.terms

import com.google.common.math.IntMath.pow
import im.ghosty.catboyaddons.Config
import im.ghosty.catboyaddons.utils.SortedHashSet
import java.util.*
import kotlin.math.floor
import kotlin.math.sqrt

fun setClickOrder(items: List<ItemData>): List<ItemData> {
    var slots = LinkedList(items.map { it.slot });
    slots = when(Config.autoTermsClickOrder) {
        0 -> HumanClickOrder.getOrder(slots)
        1 -> RandomClickOrder.getOrder(slots)
        2 -> InOrderClickOrder.getOrder(slots)
        else -> slots
    }
    val bySlot = items.associateBy { it.slot }
    return slots.mapNotNull { bySlot[it] }
}

interface ClickOrder {

    fun getOrder(correct: LinkedList<Int>): LinkedList<Int>;

}

/**
 * From top to bottom, left to right. Surely the worst method :skull:
 * even random isn't as dumb for sure (until you see it)
 */
object InOrderClickOrder : ClickOrder {

    override fun getOrder(correct: LinkedList<Int>): LinkedList<Int> {
        val clickOrder = LinkedList(correct)
        clickOrder.sortBy { i -> i }
        return clickOrder
    }

}

/**
 * MONKE
 * MONKE LOVE CLICKING
 * MONKE CLICK
 */
object RandomClickOrder : ClickOrder {

    override fun getOrder(correct: LinkedList<Int>): LinkedList<Int> {
        val clickOrder = LinkedList(correct)
        clickOrder.shuffle()
        return clickOrder
    }

}

/**
 * Humans don't click randomly, they click on whatever seems the neirest item to them,
 * to optimize their click pattern and so their speed.
 *
 * So we kinda reproduce that same behavior.
 */
object HumanClickOrder : ClickOrder {

    override fun getOrder(correct: LinkedList<Int>): LinkedList<Int> {
        val clickOrder = LinkedList<Int>()

        var last = 37; // cause WHY NOT (a bit on the left, and works with any gui size)
        var lastPosition = getPosition(last);

        val queue = SortedHashSet<Int> { id -> getDistance(getPosition(id), lastPosition) }
        queue.addAll(correct)

        while (queue.isNotEmpty()) {
            last = queue.shift()!!
            lastPosition = getPosition(last)
            clickOrder.add(last)
        }

        return clickOrder
    }

    /**
     * Get the position in the inventory of the item with the given slot id, as a pair of (column, row).
     */
    private fun getPosition(id: Int): Pair<Int, Int> {
        return Pair(id % 9, floor((id / 9).toDouble()).toInt())
    }

    /**
     * Multiplying the row by this number gives a higher priority to getting slots nearer on the Y axis like humans would do.
     */
    private val sqrt1_2 = sqrt(0.5);

    private fun getDistance(a: Pair<Int, Int>, b: Pair<Int, Int>): Double {
        return pow(a.first - b.first, 2) + pow((a.second - b.second), 2) * sqrt1_2;
    }

}