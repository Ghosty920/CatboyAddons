package im.ghosty.catboyaddons.features

import cc.polyfrost.oneconfig.events.event.ReceivePacketEvent
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import im.ghosty.catboyaddons.CatboyAddons.mc
import im.ghosty.catboyaddons.Config
import net.minecraft.network.play.server.S02PacketChat
import java.io.File
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object MessageLogger {

    private val file: File by lazy {
        mc.mcDataDir.resolve("messages.log").apply { createNewFile() }
    }

    @Subscribe
    fun onChat(event: ReceivePacketEvent) {
        if(!Config.devMessageLogger) return
        if(event.packet !is S02PacketChat) return
        val packet = event.packet as S02PacketChat
        if(packet.type.toInt() != 0) return

        val time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss"))
        file.appendText("[${time}] ${packet.chatComponent.formattedText}\n")
    }

}