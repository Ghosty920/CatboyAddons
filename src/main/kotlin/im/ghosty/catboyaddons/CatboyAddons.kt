package im.ghosty.catboyaddons

import cc.polyfrost.oneconfig.events.EventManager
import cc.polyfrost.oneconfig.utils.commands.CommandManager
import im.ghosty.catboyaddons.commands.MainCommand
import im.ghosty.catboyaddons.features.MessageLogger
import im.ghosty.catboyaddons.features.dungeons.AutoPotionBag
import im.ghosty.catboyaddons.features.dungeons.AutoRefill
import im.ghosty.catboyaddons.features.f7.CoreClip
import im.ghosty.catboyaddons.features.f7.P3AutoLeap
import im.ghosty.catboyaddons.features.f7.terms.AutoTerms
import im.ghosty.catboyaddons.features.f7.terms.TermHandler
import im.ghosty.catboyaddons.features.f7.terms.TerminalAura
import im.ghosty.catboyaddons.features.f7.terms.TerminalESP
import im.ghosty.catboyaddons.utils.Scheduler
import im.ghosty.catboyaddons.utils.ScoreboardUtils
import im.ghosty.catboyaddons.utils.StatusUtils
import im.ghosty.catboyaddons.utils.events.EventHandler
import im.ghosty.catboyaddons.utils.render.RenderUtils
import net.minecraft.client.Minecraft
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent

@Mod(
    modid = CatboyAddons.MODID,
    name = CatboyAddons.NAME,
    version = CatboyAddons.VERSION,
    modLanguageAdapter = "cc.polyfrost.oneconfig.utils.KotlinLanguageAdapter",
    clientSideOnly = true
)
object CatboyAddons {

    const val NAME = "@NAME@"
    const val MODID = "@MODID@"
    const val VERSION = "@VER@"

    const val PREFIX = "§a§l[§b§lCatboy§a§l] §7§l➔ §r"

    val mc: Minecraft = Minecraft.getMinecraft();

    @Mod.EventHandler
    fun initialize(event: FMLInitializationEvent) {
        Config.init();

        listOf(
            StatusUtils, ScoreboardUtils, Scheduler, RenderUtils, EventHandler,
            TerminalESP, AutoTerms, TermHandler, TerminalAura,
            CoreClip, P3AutoLeap,
            AutoPotionBag, AutoRefill,
            MessageLogger
        ).forEach {
            MinecraftForge.EVENT_BUS.register(it)
            EventManager.INSTANCE.register(it)
        }
        CommandManager.INSTANCE.registerCommand(MainCommand)
    }

}