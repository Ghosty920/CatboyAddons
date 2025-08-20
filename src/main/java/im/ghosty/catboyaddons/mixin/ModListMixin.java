package im.ghosty.catboyaddons.mixin;

import im.ghosty.catboyaddons.CatboyAddons;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.network.handshake.FMLHandshakeMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
@Mixin(value = FMLHandshakeMessage.ModList.class, remap = false)
public class ModListMixin {
	
	@Shadow
	private Map<String, String> modTags;
	
	@Inject(method = "<init>(Ljava/util/List;)V", at = @At("RETURN"))
	public void removeMods(List<ModContainer> modList, CallbackInfo ci) {
		if (Minecraft.getMinecraft().isIntegratedServerRunning()) // singleplayer check
			return;
		modTags.remove(CatboyAddons.MODID);
	}
	
}