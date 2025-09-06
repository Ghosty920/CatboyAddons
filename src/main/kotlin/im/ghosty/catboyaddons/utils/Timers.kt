package im.ghosty.catboyaddons.utils

class MSTimer {

    var time = 0L

    constructor() {
        this.time = System.currentTimeMillis()
    }

    constructor(time: Long) {
        this.time = time
    }

    /**
     * Resets the timer.
     */
    fun reset() {
        time = System.currentTimeMillis()
    }

    /**
     * Returns whether or not the timer has passed the given [time].
     * @return [MSTimer.timePassed] >= [time]
     */
    fun hasPassed(time: Long): Boolean {
        return timePassed() >= time
    }

    /**
     * Returns the time left needed before [MSTimer.timePassed] reaches [time].
     * @return [time] minus [MSTimer.timePassed]
     */
    fun timeLeft(time: Long): Long {
        return time - timePassed()
    }

    /**
     * Returns the time passed since the timer was last reset.
     * @return [System.currentTimeMillis] minus [MSTimer.time]
     */
    fun timePassed(): Long {
        return System.currentTimeMillis() - this.time
    }

}