package net.mcreator.enhancedenchanting;

import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.component.DataComponentType;

public class ApotheosisGemRemover {
	public static ItemStack removeGems(ItemStack stack) {
		ItemStack copy = stack.copy();
		copy.remove(
			(DataComponentType<Object>) BuiltInRegistries.DATA_COMPONENT_TYPE.get(ResourceLocation.fromNamespaceAndPath("apotheosis", "socketed_gems")
			));
		return copy;
	}
}