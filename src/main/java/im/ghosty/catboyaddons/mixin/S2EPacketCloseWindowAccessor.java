package im.ghosty.catboyaddons.mixin;

import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@SideOnly(Side.CLIENT)
@Mixin(S2EPacketCloseWindow.class)
public interface S2EPacketCloseWindowAccessor {
	
	@Accessor
	int getWindowId();
	
}
