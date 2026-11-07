package net.mcreator.enhancedenchanting.mixin;

import net.mcreator.enhancedenchanting.init.MixinVariableHandler;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Constant;

import net.minecraft.world.item.enchantment.EnchantmentHelper;

@Mixin(EnchantmentHelper.class)
public abstract class NoEnchantmentLimitMixin {
	private static final long key = MixinVariableHandler.generateKey("removeLimits");
	@ModifyConstant(
		method = "getEnchantmentCost",
		constant = @Constant(intValue = 15)
	)
	private static int change(int value) throws Throwable {
		return MixinVariableHandler.getRemoveEnchantLimit(key) ? Integer.MAX_VALUE : value;
	}
}