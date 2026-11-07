package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.Holder;
import net.minecraft.client.Minecraft;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber(value = {Dist.CLIENT})
public class AnvilLegendaryLimitDoProcedure {
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event) {
		execute(event, Minecraft.getInstance().level, event.getItemStack());
	}

	public static void execute(LevelAccessor world, ItemStack itemstack) {
		execute(null, world, itemstack);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, ItemStack itemstack) {
		if (EnhancedEnchantingModVariables.WorldVariables.get(world).DoLimit) {
			{
				ItemEnchantments _appliedEnchantments = itemstack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
				for (Holder<Enchantment> _enchantHolder : _appliedEnchantments.keySet()) {
					String stringiterator = world.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getKey(_enchantHolder.value()).toString();
					if (EnhancedEnchantingModVariables.legendary.contains(stringiterator)) {
						if (("").equals(itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("Legendary"))) {
							{
								final String _tagName = "Legendary";
								final String _tagValue = stringiterator;
								CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putString(_tagName, _tagValue));
							}
						} else if (!(stringiterator).equals(itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("Legendary"))) {
							EnchantmentHelper.updateEnchantments(itemstack, mutableEnchantments -> mutableEnchantments
									.removeIf(enchantment -> enchantment.is(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse(stringiterator))))));
						}
					}
				}
			}
		}
	}
}