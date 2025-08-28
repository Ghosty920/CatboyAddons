package im.ghosty.catboyaddons.mixin;

import im.ghosty.catboyaddons.utils.MovementHandler;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

/**
 * The reason it is split in 6 functions is pure optimization, not having to check if it's the right key.
 * We're trusting the process :)
 * @see im.ghosty.catboyaddons.utils.MovementHandler
 */
@SideOnly(Side.CLIENT)
@Mixin(MovementInputFromOptions.class)
public class MovementInputMixin {
	
	@Redirect(method = "updatePlayerMoveState", at = @At(ordinal = 0, value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z"))
	private boolean forward(KeyBinding key) {
		return MovementHandler.getAllowForward() && key.isKeyDown();
	}
	
	@Redirect(method = "updatePlayerMoveState", at = @At(ordinal = 1, value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z"))
	private boolean back(KeyBinding key) {
		return MovementHandler.getAllowBack() && key.isKeyDown();
	}
	
	@Redirect(method = "updatePlayerMoveState", at = @At(ordinal = 2, value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z"))
	private boolean left(KeyBinding key) {
		return MovementHandler.getAllowLeft() && key.isKeyDown();
	}
	
	@Redirect(method = "updatePlayerMoveState", at = @At(ordinal = 3, value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z"))
	private boolean right(KeyBinding key) {
		return MovementHandler.getAllowRight() && key.isKeyDown();
	}
	
	@Redirect(method = "updatePlayerMoveState", at = @At(ordinal = 4, value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z"))
	private boolean jump(KeyBinding key) {
		return MovementHandler.getAllowLeft() && key.isKeyDown();
	}
	
	@Redirect(method = "updatePlayerMoveState", at = @At(ordinal = 5, value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z"))
	private boolean sneak(KeyBinding key) {
		return MovementHandler.getAllowSneak() && key.isKeyDown();
	}
	
}
