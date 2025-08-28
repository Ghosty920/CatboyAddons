package im.ghosty.catboyaddons.utils

class MSTimer {

    var time = 0L

    constructor() {
        this.time = System.currentTimeMillis()
    }

    constructor(time: Long) {
        this.time = time
    }

    fun reset() {
        time = System.currentTimeMillis()
    }

    fun hasPassed(time: Long): Boolean {
        return timePassed() >= time
    }

    fun timePassed(): Long {
        return System.currentTimeMillis() - this.time
    }

}