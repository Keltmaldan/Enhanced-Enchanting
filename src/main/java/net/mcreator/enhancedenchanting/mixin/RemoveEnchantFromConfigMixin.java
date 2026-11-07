package net.mcreator.enhancedenchanting.mixin;

import net.mcreator.enhancedenchanting.EnchantmentModificationReader;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.At;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;
import java.util.List;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

@Mixin(EnchantmentHelper.class)
public abstract class RemoveEnchantFromConfigMixin {
	@ModifyVariable(
		//method name is looooooooooooooonnnnnnnnnnnnnnng
		method = "getAvailableEnchantmentResults(ILnet/minecraft/world/item/ItemStack;Ljava/util/stream/Stream;)Ljava/util/List;",
		at = @At("HEAD"),
		argsOnly = true,
		ordinal = 0
	)
	private static Stream<Holder<Enchantment>> change(Stream<Holder<Enchantment>> original) {
		return original.filter(enchantment ->
            enchantment.unwrapKey()
                .map(key -> !(EnchantmentModificationReader.toRemove
                    .contains(key.location().toString()) || EnchantmentModificationReader.toRemove
                    .contains("all")))
                .orElse(true));
	}
}