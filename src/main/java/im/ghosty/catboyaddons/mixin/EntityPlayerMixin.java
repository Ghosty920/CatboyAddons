package im.ghosty.catboyaddons.mixin;

import im.ghosty.catboyaddons.Config;
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
import net.minecraftforge.common.util.Constants;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.HashSet;
import java.util.Set;

@SideOnly(Side.CLIENT)
@Mixin(EntityPlayer.class)
public abstract class EntityPlayerMixin {

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
        if (!catboyAddons$isShortbow(item)) return;
        itemInUse = null;
        itemInUseCount = 0;
    }

    @Unique
    private static final Set<String> catboyAddons$bowCache = new HashSet<>();

    @Unique
    private boolean catboyAddons$isShortbow(ItemStack item) {
        if (item == null || !(item.getItem() instanceof ItemBow) || !item.hasTagCompound()) return false;

        String id = null;
        NBTTagCompound extraAttributes = item.getSubCompound("ExtraAttributes", false);
        if(extraAttributes != null && extraAttributes.hasKey("id")) {
            id = extraAttributes.getString("id");
        }

        if (id == null) return false;
        if (catboyAddons$bowCache.contains(id)) return true;

        NBTTagCompound display = item.getTagCompound().getCompoundTag("display");
        if (!display.hasKey("Lore", Constants.NBT.TAG_LIST)) return false;

        NBTTagList loreNBT = display.getTagList("Lore", Constants.NBT.TAG_STRING);

        for (int i = 0; i < loreNBT.tagCount(); i++) {
            String line = loreNBT.getStringTagAt(i);
            if (line.contains("Shortbow: Instantly shoots!")) {
                catboyAddons$bowCache.add(id);
                return true;
            }
        }

        return false;
    }
}
