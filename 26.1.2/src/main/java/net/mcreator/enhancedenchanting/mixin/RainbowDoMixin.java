package net.mcreator.enhancedenchanting.mixin;

import net.mcreator.enhancedenchanting.RainbowHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class RainbowDoMixin {
    @Inject(method = "inventoryTick", at = @At("TAIL"))
    private void enhancedEnchanting$rainbow(Level level, Entity owner, EquipmentSlot slot, CallbackInfo ci) {
        if (level.isClientSide() || owner.tickCount % 20 != 0 || slot == null) return;
        if (slot != EquipmentSlot.HEAD && slot != EquipmentSlot.CHEST
                && slot != EquipmentSlot.LEGS && slot != EquipmentSlot.FEET) return;
        RainbowHelper.execute(level, owner, (ItemStack)(Object)this);
    }
}