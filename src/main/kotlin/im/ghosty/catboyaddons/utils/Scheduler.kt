package im.ghosty.catboyaddons.utils

import im.ghosty.catboyaddons.CatboyAddons.mc
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import java.util.concurrent.CopyOnWriteArrayList

object Scheduler {

    private var ticksPassed: Int = 0
    private val tasks = CopyOnWriteArrayList<Task>()

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase !== TickEvent.Phase.START) return

        tasks.removeAll {
            if (it.delay-- <= 0) {
                mc.addScheduledTask { it.callback() }
                true
            } else false
        }
    }

    fun add(callback: () -> Unit) {
        add(0, callback)
    }

    fun add(delay: Int, callback: () -> Unit) {
        tasks.add(Task(delay, callback))
    }

    fun addMS(delay: Int, callback: () -> Unit) {
        Thread {
            Thread.sleep(delay.toLong())
            callback()
        }.start()
    }

}

open class Task(var delay: Int, val callback: () -> Unit)