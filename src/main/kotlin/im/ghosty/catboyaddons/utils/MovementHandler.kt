package im.ghosty.catboyaddons.utils

import cc.polyfrost.oneconfig.utils.dsl.mc
import net.minecraft.client.settings.KeyBinding
import org.lwjgl.input.Keyboard

object MovementHandler {

    fun stop() {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.keyCode, false)
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.keyCode, false)
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.keyCode, false)
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.keyCode, false)
    }

    fun stopAll() {
        stop()
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.keyCode, false)
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.keyCode, false)
    }

    fun restore() {
        restoreKey(mc.gameSettings.keyBindForward.keyCode)
        restoreKey(mc.gameSettings.keyBindBack.keyCode)
        restoreKey(mc.gameSettings.keyBindLeft.keyCode)
        restoreKey(mc.gameSettings.keyBindRight.keyCode)
    }

    fun restoreAll() {
        restore()
        restoreKey(mc.gameSettings.keyBindJump.keyCode)
        restoreKey(mc.gameSettings.keyBindSneak.keyCode)
    }

    private fun restoreKey(key: Int) {
        KeyBinding.setKeyBindState(key, Keyboard.isKeyDown(key))
    }

}