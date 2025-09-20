package im.ghosty.catboyaddons.features.f7.terms

import im.ghosty.catboyaddons.utils.events.PacketReceiveEvent
import cc.polyfrost.oneconfig.events.event.Stage
import cc.polyfrost.oneconfig.events.event.TickEvent
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import im.ghosty.catboyaddons.CatboyAddons.mc
import im.ghosty.catboyaddons.Config
import im.ghosty.catboyaddons.utils.LocUtils.getEyeDistanceToEntity
import im.ghosty.catboyaddons.utils.StatusUtils
import im.ghosty.catboyaddons.utils.Utils.removeFormatting
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.network.play.client.C02PacketUseEntity
import net.minecraft.network.play.server.S02PacketChat
import net.minecraft.network.play.server.S32PacketConfirmTransaction
import net.minecraft.util.Vec3

object TerminalAura {

    var clickDelay = 0

    @Subscribe
    fun onTick(event: TickEvent) {
        if (event.stage !== Stage.START) return
        if (Config.secure || !Config.terminalAura) return
        if (StatusUtils.dungeonF7Phase != 3) return
        if (mc.thePlayer == null || mc.theWorld == null) return

        if (TermHandler.inTerminal) return
        if(clickDelay > 0) return

        if(Config.terminalAuraGroundOnly && !mc.thePlayer.onGround) return

        mc.theWorld.loadedEntityList
            .filterIsInstance<EntityArmorStand>()
            .filter { it.name.removeFormatting() == "Inactive Terminal" }
            .firstOrNull { mc.thePlayer.getEyeDistanceToEntity(it) < Config.terminalAuraReach }?.run {
                val packet = C02PacketUseEntity(this, Vec3(0.0, 0.0, 0.0))
                mc.thePlayer.sendQueue.addToSendQueue(packet)
                clickDelay = 20
            }
    }

    @Subscribe(priority = 1000000000)
    fun onPacket(event: PacketReceiveEvent) {
        if (event.packet is S32PacketConfirmTransaction) {
            if (clickDelay > 0) clickDelay--
            return
        }

        if (event.packet is S02PacketChat) {
            if (event.packet.type.toInt() != 0) return
            if (event.packet.chatComponent.unformattedText.removeFormatting() != "This Terminal doesn't seem to be responsive at the moment.") return
            println("Terminal not responsive found! gg peak")
            clickDelay = 0
            return
        }
    }

}