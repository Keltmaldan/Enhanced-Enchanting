package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;

public class SoulStrenghtRendererProcedure {
	public static void execute(LevelAccessor world, ItemStack itemstack) {
		if (world == null)
			return;
		if (itemstack.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, Identifier.parse("enhanced_enchanting:soul_catalyst")))) != 0) {
		}
	}
}