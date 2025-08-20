package im.ghosty.catboyaddons

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType

object Config : Config(Mod(CatboyAddons.NAME, ModType.SKYBLOCK), "${CatboyAddons.MODID}.json") {

    @JvmField
    @Switch(name = "Shortbow Pull Fix", description = "Fixes shortbows pulling back when you have an arrow in your inventory", size = 2, category = "General")
    var shortbowPullFix = false

    @JvmField
    @Switch(name = "Toggle P3 AutoLeap", description = "Automatically leap to the best person possible in F7 P3 (Maxor)", size = 2, category = "F7", subcategory = "Phase 3 - Maxor")
    var p3AutoLeap = false
    @JvmField
    @Switch(name = "Healer -> I4", description = "For Healer (NO CHECK IMPLEMENTED), leap to whoever is doing I4", size = 1, category = "F7", subcategory = "Phase 3 - Maxor")
    var p3AutoLeapI4 = false
    @JvmField
    @Switch(name = "Simon Says Camp", description = "For everyone, leap to whoever is at SSC", size = 1, category = "F7", subcategory = "Phase 3 - Maxor")
    var p3AutoLeapSSC = true
    @JvmField
    @Switch(name = "Early Entry 2", description = "For everyone, leap to whoever is doing ee2", size = 1, category = "F7", subcategory = "Phase 3 - Maxor")
    var p3AutoLeapEE2 = true
    @JvmField
    @Switch(name = "Early Entry 3", description = "For everyone, leap to whoever is doing ee3", size = 1, category = "F7", subcategory = "Phase 3 - Maxor")
    var p3AutoLeapEE3 = true
    @JvmField
    @Switch(name = "Early Entry 4 / Core", description = "For everyone, leap to whoever is doing core/ee4", size = 1, category = "F7", subcategory = "Phase 3 - Maxor")
    var p3AutoLeapEE4 = true
    @JvmField
    @Switch(name = "Tunnel", description = "For everyone, leap to whoever is in the tunnel before Maxor angry 3:", size = 1, category = "F7", subcategory = "Phase 3 - Maxor")
    var p3AutoLeapTunnel = true

    @JvmField
    @Switch(name = "Toggle Message Logger", description = "Logs messages sent to the chat (.minecraft/messages.log)", size = 2, category = "Dev")
    var devMessageLogger = false

}