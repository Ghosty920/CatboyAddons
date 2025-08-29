package im.ghosty.catboyaddons.utils

object MovementHandler {

    private var stopMovement = 0;
    private var stopJump = 0;
    private var stopSneak = 0;

    @JvmStatic
    fun canMove() = stopMovement == 0

    @JvmStatic
    fun canJump() = stopJump == 0

    @JvmStatic
    fun canSneak() = stopSneak == 0

    fun stop() = stopMovement++
    fun stopJump() = stopJump++
    fun stopSneak() = stopSneak++

    fun stopAll() {
        stop()
        stopJump()
        stopSneak()
    }

    fun restore() {
        stopMovement = (stopMovement - 1).coerceAtLeast(0)
    }

    fun restoreJump() {
        stopJump = (stopJump - 1).coerceAtLeast(0)
    }

    fun restoreSneak() {
        stopSneak = (stopSneak - 1).coerceAtLeast(0)
    }

    fun restoreAll() {
        restore()
        restoreJump()
        restoreSneak()
    }

}