package im.ghosty.catboyaddons.features

import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import im.ghosty.catboyaddons.CatboyAddons.mc
import im.ghosty.catboyaddons.Config
import im.ghosty.catboyaddons.utils.events.PacketReceiveEvent
import im.ghosty.catboyaddons.utils.events.PacketSendEvent
import net.minecraft.network.play.client.C01PacketChatMessage
import net.minecraft.network.play.server.S02PacketChat
import java.io.File
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object MessageLogger {

    private val file: File by lazy {
        mc.mcDataDir.resolve("messages.log").apply { createNewFile() }
    }

    @Subscribe
    fun onChat(event: PacketReceiveEvent) {
        if (!Config.devMessageLogger) return
        val packet = event.packet as? S02PacketChat ?: return
        if (packet.type.toInt() != 0) return

        val time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss"))
        file.appendText("[${time}] ${packet.chatComponent.formattedText}\n")
    }

    @Subscribe
    fun onCommand(event: PacketSendEvent) {
        if (!Config.devMessageLogger) return
         val packet = event.packet as? C01PacketChatMessage ?: return
        if (packet.message.startsWith("/") && packet.message.endsWith("-----")) return

        val time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss"))
        file.appendText("[${time}]+ ${packet.message}\n")
    }

}