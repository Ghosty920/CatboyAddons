package im.ghosty.catboyaddons.utils

object MovementHandler {

    @JvmStatic
    var allowForward = true
    @JvmStatic
    var allowBack = true
    @JvmStatic
    var allowLeft = true
    @JvmStatic
    var allowRight = true
    @JvmStatic
    var allowJump = true
    @JvmStatic
    var allowSneak = true

    fun stop() {
        allowForward = false
        allowBack = false
        allowLeft = false
        allowRight = false
    }

    fun stopAll() {
        stop()
        allowJump = false
        allowSneak = false
    }

    fun restore() {
        allowForward = true
        allowBack = true
        allowLeft = true
        allowRight = true
    }

    fun restoreAll() {
        restore()
        allowJump = true
        allowSneak = true
    }

}