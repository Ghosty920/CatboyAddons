package im.ghosty.catboyaddons

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.Checkbox
import cc.polyfrost.oneconfig.config.annotations.Color
import cc.polyfrost.oneconfig.config.annotations.Dropdown
import cc.polyfrost.oneconfig.config.annotations.DualOption
import cc.polyfrost.oneconfig.config.annotations.Info
import cc.polyfrost.oneconfig.config.annotations.Slider
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.core.OneColor
import cc.polyfrost.oneconfig.config.data.InfoType
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType

object Config : Config(Mod(CatboyAddons.NAME, ModType.SKYBLOCK), "${CatboyAddons.MODID}.json") {

    @JvmField
    @DualOption(name = "Security Modules", description = "Disable the possibility to modify some settings, and prevent those from working", left = "None", right = "Spec Safe", size = 2, category = "General", subcategory = "Settings")
    var secure = true

    @JvmField
    @Switch(name = "Auto Sprint", description = "Guess! :)", size = 2, category = "General", subcategory = "Features")
    var autoSprint = false
    @JvmField
    @Switch(name = "Shortbow Pull Fix", description = "Fixes shortbows pulling back when you have an arrow in your inventory", size = 2, category = "General", subcategory = "Features")
    var shortbowPullFix = false

    @JvmField
    @Dropdown(name = "Auto Refill Pearls", description = "Automatically pick Ender Pearls from stash if you're below or at 8", options = ["Never", "In a Run", "Always"], size = 1, category = "Dungeons", subcategory = "QoL")
    var autoRefillPearls = 0
    @JvmField
    @Switch(name = "Auto Refill Jerries", description = "Automatically peak Inflatable Jerries from stash if you're below or at 32", size = 1, category = "Dungeons", subcategory = "QoL")
    var autoRefillJerries = false
    @JvmField
    @Switch(name = "Auto Potion Bag", description = "Automatically open the potion bag at the start of a run", size = 2, category = "Dungeons", subcategory = "QoL")
    var autoOpenPotionBag = false

    @JvmField
    @Switch(name = "Core Clip", description = "Lets you go through the gold door (core entrance) without any effort", size = 2, category = "F7", subcategory = "Phase 3")
    var coreClip = false

    @JvmField
    @Switch(name = "Terminal ESP", description = "Render inactive terminals", size = 2, category = "F7", subcategory = "Phase 3 - Terminal ESP")
    var terminalESP = false
    @JvmField
    @Color(name = "Outline Color", description = "The color for the outside box", category = "F7", subcategory = "Phase 3 - Terminal ESP")
    var terminalESPOutlineColor = OneColor(0xFFFF2222.toInt())
    @JvmField
    @Color(name = "Fill Color", description = "The color for the inside box", category = "F7", subcategory = "Phase 3 - Terminal ESP")
    var terminalESPFillColor = OneColor(0x00FF2222)
    @JvmField
    @Slider(name = "Line Width", description = "The delay to wait before the very first click", min = 1f, max = 5f, step = 1, category = "F7", subcategory = "Phase 3 - Terminal ESP")
    var terminalESPLineWidth = 2f

    @Info(text = "TermAura is at your own risk for the moment", type = InfoType.ERROR, category = "F7", subcategory = "Phase 3 - Terminal Aura")
    private var _noteTermAura = false
    @Switch(name = "Terminal Aura", description = "Opens terminals automatically", size = 2, category = "F7", subcategory = "Phase 3 - Terminal Aura")
    var terminalAura = false
    @Slider(name = "Reach", description = "From how far you can interact with the terminal", min = 2f, max = 6f, category = "F7", subcategory = "Phase 3 - Terminal Aura")
    var terminalAuraReach = 5f
    @Switch(name = "Ground Only", description = "Will only attempt to open it when on ground", size = 2, category = "F7", subcategory = "Phase 3 - Terminal Aura")
    var terminalAuraGroundOnly = false
    @Slider(name = "Max Fov", description = "The maximum FOV away from the terminal you can have. Hope that makes any sense! :)", min = 10f, max = 360f, category = "F7", subcategory = "Phase 3 - Terminal Aura")
    var terminalAuraFov = 360f

    @Info(text = "AutoTerms is at your own risk for the moment", type = InfoType.ERROR, category = "F7", subcategory = "Phase 3 - Auto Terms")
    private var _noteAutoTerms = false
    @JvmField
    @Switch(name = "AutoTerms", description = "Automatically do terminals by clicking correct slots", size = 2, category = "F7", subcategory = "Phase 3 - Auto Terms")
    var autoTerms = false
    @JvmField
    @Slider(name = "Click Delay", description = "The delay to wait between each click", min = 50f, max = 500f, category = "F7", subcategory = "Phase 3 - Auto Terms")
    var autoTermsClickDelay = 175
    @JvmField
    @Slider(name = "First Click Delay", description = "The delay to wait before the very first click", min = 100f, max = 800f, category = "F7", subcategory = "Phase 3 - Auto Terms")
    var autoTermsFirstClickDelay = 450
    @JvmField
    @Dropdown(name = "Click Order", description = "The way the click order will be calculated", options = ["Human-like/Distance", "Random", "Linear"], size = 1, category = "F7", subcategory = "Phase 3 - Auto Terms")
    var autoTermsClickOrder = 0
    @JvmField
    @Switch(name = "Reverse Order", description = "The same calculated order, but in reverse to add randomness", size = 1, category = "F7", subcategory = "Phase 3 - Auto Terms")
    var autoTermsReverseOrder = false
    @JvmField
    @Slider(name = "Break Threshold", description = "After how much time it assumes that you lagged or some issue happened", min = 500f, max = 3000f, category = "F7", subcategory = "Phase 3 - Auto Terms")
    var autoTermsBreakThreshold = 1000
    @JvmField
    @Checkbox(name = "InvWalk", description = "Removes the inventory to let you walk freely around", size = 1, category = "F7", subcategory = "Phase 3 - Auto Terms")
    var autoTermsInvWalk = false
    @JvmField
    @Checkbox(name = "InvWalk Melody", description = "Removes the melody inventory to let you walk freely around", size = 1, category = "F7", subcategory = "Phase 3 - Auto Terms")
    var autoTermsInvWalkMelody = false
    @JvmField
    @Slider(name = "InvWalk Melody Safe Delay", description = "Prevents you from moving after clicking in melody to not get sent to limbo", min = 50f, max = 1000f, category = "F7", subcategory = "Phase 3 - Auto Terms")
    var autoTermsInvWalkMelodySafe = 300

    @Info(text = "AutoLeap works by automatically finding the best person. You cannot specify a class/person.", type = InfoType.WARNING, size = 2, category = "F7", subcategory = "Phase 3 - Auto Leap")
    private var _noteAutoLeap = false
    @JvmField
    @Switch(name = "Toggle P3 AutoLeap", description = "Automatically leap to the best person possible at the right timing", size = 2, category = "F7", subcategory = "Phase 3 - Auto Leap")
    var p3AutoLeap = false
    @JvmField
    @Checkbox(name = "Healer -> I4", description = "Leap to whoever is doing I4 (at first Storm crush)", size = 1, category = "F7", subcategory = "Phase 3 - Auto Leap")
    var p3AutoLeapI4 = false
    @JvmField
    @Checkbox(name = "Simon Says Camp", description = "Leap to whoever is at SSC (when Storm is done)", size = 1, category = "F7", subcategory = "Phase 3 - Auto Leap")
    var p3AutoLeapSSC = true
    @JvmField
    @Checkbox(name = "Early Entry 2", description = "Leap to whoever is doing ee2 (at 7/7)", size = 1, category = "F7", subcategory = "Phase 3 - Auto Leap")
    var p3AutoLeapEE2 = true
    @JvmField
    @Checkbox(name = "Early Entry 3", description = "Leap to whoever is doing ee3 (at 8/8)", size = 1, category = "F7", subcategory = "Phase 3 - Auto Leap")
    var p3AutoLeapEE3 = true
    @JvmField
    @Checkbox(name = "Early Entry 4 / Core", description = "Leap to whoever is doing core/ee4 (at 7/7)", size = 1, category = "F7", subcategory = "Phase 3 - Auto Leap")
    var p3AutoLeapEE4 = true
    @JvmField
    @Checkbox(name = "Tunnel", description = "Leap to whoever is in the tunnel before Maxor angy 3:", size = 1, category = "F7", subcategory = "Phase 3 - Auto Leap")
    var p3AutoLeapTunnel = true

    @JvmField
    @Switch(name = "Toggle Message Logger", description = "Logs messages sent to the chat (.minecraft/messages.log)", size = 2, category = "Dev")
    var devMessageLogger = false
    @JvmField
    @Switch(name = "Status Lie", description = "Lie about your status :(", size = 2, category = "Dev")
    var devStatusLiar = false

    fun init() {
        initialize()

        notSecure("coreClip")
        notSecure("terminalAura")
        notSecure("terminalAuraReach")
        notSecure("terminalAuraGroundOnly")
        notSecure("terminalAuraFov")
        notSecure("autoTermsInvWalk")
        notSecure("autoTermsInvWalkMelody")
        notSecure("autoTermsInvWalkMelodySafe")

        addDependency("terminalESPOutlineColor", "terminalESP")
        addDependency("terminalESPFillColor", "terminalESP")
        addDependency("terminalESPLineWidth", "terminalESP")
        todo("terminalESPFillColor")

        addDependency("terminalAuraReach", "terminalAura")
        addDependency("terminalAuraGroundOnly", "terminalAura")
        addDependency("terminalAuraFov", "terminalAura")
        todo("terminalAuraFov")

        addDependency("autoTermsClickDelay", "autoTerms")
        addDependency("autoTermsFirstClickDelay", "autoTerms")
        addDependency("autoTermsClickOrder", "autoTerms")
        addDependency("autoTermsReverseDelay", "autoTerms")
        addDependency("autoTermsBreakThreshold", "autoTerms")
        addDependency("autoTermsInvWalk", "autoTerms")
        addDependency("autoTermsInvWalkMelody", "autoTerms")
        addDependency("autoTermsInvWalkMelodySafe", "autoTerms")
        addDependency("autoTermsInvWalkMelodySafe", "autoTermsInvWalkMelody")

        addDependency("p3AutoLeapI4", "p3AutoLeap")
        addDependency("p3AutoLeapSSC", "p3AutoLeap")
        addDependency("p3AutoLeapEE2", "p3AutoLeap")
        addDependency("p3AutoLeapEE3", "p3AutoLeap")
        addDependency("p3AutoLeapEE4", "p3AutoLeap")
        addDependency("p3AutoLeapTunnel", "p3AutoLeap")
    }

    private fun notSecure(option: String) {
        optionNames[option]?.addDependency("Security Modules") {
            return@addDependency !secure
        }
    }

    @Deprecated("Implement it at some point...", level = DeprecationLevel.WARNING)
    private fun todo(option: String) {
        optionNames[option]?.addDependency("Not done yet!") {
            return@addDependency false
        }
    }

}