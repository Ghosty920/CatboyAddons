import im.ghosty.catboyaddons.features.f7.terms.HumanClickOrder
import im.ghosty.catboyaddons.features.f7.terms.InOrderClickOrder
import im.ghosty.catboyaddons.features.f7.terms.RandomClickOrder
import java.util.*
import kotlin.random.Random

object ClickOrderTest {

    val size = 4 * 9;

    @JvmStatic
    fun main(args: Array<String>) {
        inOrderTest()
        randomTest()
        humanTest()
    }

    fun inOrderTest() {
        val list = getRandomList()
        println("In Order Testing:")
        println(list)
        println(getRepresentation(InOrderClickOrder.getOrder(list)))
        println()
    }

    fun randomTest() {
        val list = getRandomList()
        println("Random Testing:")
        println(list)
        println(getRepresentation(RandomClickOrder.getOrder(list)))
        println()
    }

    fun humanTest() {
        val list = getRandomList()
        println("Human Testing:")
        println(list)
        println(getRepresentation(HumanClickOrder.getOrder(list)))
        println()
    }

    /*
     *
     * UTILS
     *
     */

    fun getRandomList(): LinkedList<Int> {
        val list = LinkedList<Int>();

        var remaining = Random.nextInt(9) + 5;
        while (remaining > 0) {
            val slot = Random.nextInt(size)
            if (list.contains(slot)) continue;
            list.add(slot);
            remaining--;
        }

        return list;
    }

    fun getRepresentation(list: LinkedList<Int>): String {
        val orderByIndex = HashMap<Int, Int>(list.size)
        list.forEachIndexed { idx, value ->
            if (value in 0..<size) {
                orderByIndex[value] = idx
            }
        }

        val chars = CharArray(size) { '-' }
        for ((position, order) in orderByIndex) {
            chars[position] = when (order) {
                in 0..9 -> order.digitToChar()
                in 10..36 -> "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()[order - 10]
                else -> '-'
            }
        }

        return String(chars).chunked(9).joinToString("\n")
    }

}