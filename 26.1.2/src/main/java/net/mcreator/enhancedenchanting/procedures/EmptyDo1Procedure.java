package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.world.item.ItemStack;

import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModItems;

public class EmptyDo1Procedure {
	public static void execute(ItemStack itemstack) {
		if (itemstack.getItem() == EnhancedEnchantingModItems.EMPTY_STACK.get()) {
			itemstack.setCount(0);
		}
	}
}