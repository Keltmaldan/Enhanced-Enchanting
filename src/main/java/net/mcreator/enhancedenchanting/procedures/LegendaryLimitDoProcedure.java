package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.Holder;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class LegendaryLimitDoProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity().level(), event.getEntity());
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		double itemnumber = 0;
		ItemStack item = ItemStack.EMPTY;
		String text_render = "";
		MutableComponent new_name = Component.empty();
		if (entity.getCapability(Capabilities.ItemHandler.ENTITY, null) instanceof IItemHandlerModifiable _modHandlerIter) {
			for (int _idx = 0; _idx < _modHandlerIter.getSlots(); _idx++) {
				ItemStack itemstackiterator = _modHandlerIter.getStackInSlot(_idx).copy();
				if (EnhancedEnchantingModVariables.WorldVariables.get(world).DoLimit) {
					{
						ItemStack _redwire$itemstack = item;
						ItemEnchantments _appliedEnchantments = _redwire$itemstack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
						for (Holder<Enchantment> _enchantHolder : _appliedEnchantments.keySet()) {
							String stringiterator = world.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getKey(_enchantHolder.value()).toString();
							if (EnhancedEnchantingModVariables.legendary.contains(stringiterator)) {
								if (("").equals(item.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("Legendary"))) {
									{
										final String _tagName = "Legendary";
										final String _tagValue = stringiterator;
										CustomData.update(DataComponents.CUSTOM_DATA, item, tag -> tag.putString(_tagName, _tagValue));
									}
								} else if (!(stringiterator).equals(item.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("Legendary"))) {
									EnchantmentHelper.updateEnchantments(item, mutableEnchantments -> mutableEnchantments
											.removeIf(enchantment -> enchantment.is(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse(stringiterator))))));
								}
							}
						}
					}
				}
			}
		}
	}
}