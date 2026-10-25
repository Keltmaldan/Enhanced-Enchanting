package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.event.entity.player.CanPlayerSleepEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class RestorativeSleepDoProcedure {
	@SubscribeEvent
	public static void onPlayerInBed(CanPlayerSleepEvent event) {
		execute(event, event.getEntity().level(), event.getEntity());
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		double slot = 0;
		ItemStack new_slot = ItemStack.EMPTY;
		if (!entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).AlreadySlept && !(world instanceof Level _lvl0 && _lvl0.isDay())) {
			slot = 0;
			for (int _i1 = 0; _i1 < 40; _i1++) {
				if ((entity.getCapability(Capabilities.ItemHandler.ENTITY, null) instanceof IItemHandlerModifiable _modHandler1 ? _modHandler1.getStackInSlot((int) slot).copy() : ItemStack.EMPTY)
						.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:restorative_sleep")))) != 0) {
					new_slot = (entity.getCapability(Capabilities.ItemHandler.ENTITY, null) instanceof IItemHandlerModifiable _modHandler3 ? _modHandler3.getStackInSlot((int) slot).copy() : ItemStack.EMPTY).copy();
					new_slot.setDamageValue(
							(int) Math.max(
									new_slot.getDamageValue() - 10 * (entity.getCapability(Capabilities.ItemHandler.ENTITY, null) instanceof IItemHandlerModifiable _modHandler5 ? _modHandler5.getStackInSlot((int) slot).copy() : ItemStack.EMPTY)
											.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:restorative_sleep")))),
									0));
					if (entity.getCapability(Capabilities.ItemHandler.ENTITY, null) instanceof IItemHandlerModifiable _modHandler) {
						ItemStack _setstack = new_slot.copy();
						_setstack.setCount(1);
						_modHandler.setStackInSlot((int) slot, _setstack);
					}
				}
				slot = slot + 1;
			}
			{
				EnhancedEnchantingModVariables.PlayerVariables _vars = entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES);
				_vars.AlreadySlept = true;
				_vars.markSyncDirty();
			}
		}
	}
}