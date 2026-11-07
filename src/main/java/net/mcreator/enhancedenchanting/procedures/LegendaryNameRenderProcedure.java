package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.Holder;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class LegendaryNameRenderProcedure {
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
		boolean leg = false;
		MutableComponent name = Component.empty();
		String initial = "";
		double colorStep = 0;
		double color = 0;
		if (entity.tickCount % 20 != 0)
			return;
		if (EnhancedEnchantingModVariables.MapVariables.get(world).ModifyNameOfItem) {
			if (entity.getCapability(Capabilities.ItemHandler.ENTITY, null) instanceof IItemHandlerModifiable _modHandlerIter) {
				for (int _idx = 0; _idx < _modHandlerIter.getSlots(); _idx++) {
					ItemStack itemstackiterator = _modHandlerIter.getStackInSlot(_idx);
					if (itemstackiterator.isEnchanted()) {
						if (itemstackiterator.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBoolean("has_rendered_name")) {
							continue;
						}
					} else {
						continue;
					}
					leg = false;
					{
						ItemStack _redwire$itemstack = itemstackiterator;
						ItemEnchantments _appliedEnchantments = _redwire$itemstack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
						for (Holder<Enchantment> _enchantHolder : _appliedEnchantments.keySet()) {
							String stringiterator = world.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getKey(_enchantHolder.value()).toString();
							if (EnhancedEnchantingModVariables.legendary.contains(stringiterator)) {
								leg = true;
								break;
							}
						}
					}
					if (leg) {
						{
							final String _tagName = "has_rendered_name";
							final boolean _tagValue = true;
							CustomData.update(DataComponents.CUSTOM_DATA, itemstackiterator, tag -> tag.putBoolean(_tagName, _tagValue));
						}
						initial = ((itemstackiterator.getDisplayName().getString()).replace("]", "")).replace("[", "");
						itemstackiterator.set(DataComponents.ITEM_NAME, ReturnLegendaryTextColor3Procedure.execute(initial));
					}
				}
			}
		}
	}
}