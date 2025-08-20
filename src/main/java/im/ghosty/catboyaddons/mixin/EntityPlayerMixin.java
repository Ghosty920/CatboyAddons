package im.ghosty.catboyaddons.mixin;

import im.ghosty.catboyaddons.Config;
import im.ghosty.catboyaddons.utils.ItemUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SideOnly(Side.CLIENT)
@Mixin(EntityPlayer.class)
public class EntityPlayerMixin {
	
	@Shadow
	public InventoryPlayer inventory;
	@Shadow
	private ItemStack itemInUse;
	@Shadow
	private int itemInUseCount;
	
	@Inject(method = "onUpdate", at = @At("HEAD"))
	private void onUpdate$shortbowPullFix(CallbackInfo ci) {
		if (!Config.shortbowPullFix) return;
		if (!((EntityPlayer) (Object) this instanceof EntityPlayerSP)) return;
		if (itemInUse == null || inventory == null) return;
		ItemStack item = inventory.getCurrentItem();
		if (item == null || !(item.getItem() instanceof ItemBow)) return;
		String id = ItemUtils.INSTANCE.getItemId(item);
		if (!id.contains("SHORTBOW") && !id.contains("TERMINATOR") && !id.contains("ITEM_SPIRIT_BOW") && !id.contains("MOSQUITO_BOW") && !id.contains("MACHINE_GUN_BOW"))
			return;
		itemInUse = null;
		itemInUseCount = 0;
	}
	
}
