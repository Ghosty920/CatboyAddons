package im.ghosty.catboyaddons.utils

class SortedHashSet<K>(private val score: (K) -> Double) : HashSet<K>() {

    fun get(index: Int): K? {
        return this.sortedBy { score(it) }.getOrNull(index)
    }

    fun getFirst(): K? {
        return get(0);
    }

    fun getLast(): K? {
        return get(size - 1);
    }

    fun shift(): K? {
        val k = getFirst()
        if(k !== null) this.remove(k)
        return k
    }

    fun pop(): K? {
        val k = getLast()
        if(k !== null) this.remove(k)
        return k
    }

}
