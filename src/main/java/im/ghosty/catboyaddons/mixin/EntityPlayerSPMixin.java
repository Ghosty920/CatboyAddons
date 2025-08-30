package im.ghosty.catboyaddons.mixin;

import im.ghosty.catboyaddons.Config;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = EntityPlayerSP.class, priority = 1000000000)
public class EntityPlayerSPMixin {
	
	@Redirect(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z"))
	private boolean setSprintState(KeyBinding key) {
		return Config.autoSprint || key.isKeyDown();
	}
	
}
