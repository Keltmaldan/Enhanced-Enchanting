package net.mcreator.enhancedenchanting.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.mcreator.enhancedenchanting.RainbowHelper;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;

@Mixin(ItemStack.class)
public class RainbowDoMixin {
	@Inject(
		method = "inventoryTick",
		at = @At("TAIL"),
		cancellable = false
	)
	private void method(Level level, Entity owner, int slot, boolean selected, CallbackInfo ci) {
		if (owner.tickCount % 20 != 0 || selected || slot < 36 || slot > 39 || level.isClientSide())
			return;
		ItemStack stack = (ItemStack) (Object) this;
		RainbowHelper.execute(level, owner, stack);
	}
}