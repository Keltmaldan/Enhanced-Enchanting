package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.event.entity.player.CanPlayerSleepEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
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
		if (!entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).AlreadySlept && !(world instanceof Level _lvl0 && _lvl0.isBrightOutside())) {
			slot = 0;
			for (int _i1 = 0; _i1 < 40; _i1++) {
				if ((getEntitySlot(entity, (int) slot))
						.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, Identifier.parse("enhanced_enchanting:restorative_sleep")))) != 0) {
					new_slot = (getEntitySlot(entity, (int) slot)).copy();
					new_slot.setDamageValue((int) Math.max(new_slot.getDamageValue() - 10 * (getEntitySlot(entity, (int) slot))
							.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, Identifier.parse("enhanced_enchanting:restorative_sleep")))), 0));
					if (entity.getCapability(Capabilities.Item.ENTITY, null) instanceof ResourceHandler<ItemResource> _resourceHandler) {
						setStackInSlot(_resourceHandler, (int) slot, ItemResource.of(new_slot), 1);
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

	private static ItemStack getEntitySlot(Entity entity, int slot) {
		if (entity != null) {
			ResourceHandler<ItemResource> resourceHandler = entity.getCapability(Capabilities.Item.ENTITY, null);
			if (resourceHandler != null) {
				return ItemUtil.getStack(resourceHandler, slot);
			}
		}
		return ItemStack.EMPTY;
	}

	private static void setStackInSlot(ResourceHandler<ItemResource> handler, int index, ItemResource resource, int amount) {
		try (var tx = Transaction.openRoot()) {
			if (!handler.getResource(index).isEmpty())
				handler.extract(index, handler.getResource(index), handler.getAmountAsInt(index), tx);
			if (!resource.isEmpty() && amount > 0)
				handler.insert(index, resource, amount, tx);
			tx.commit();
		}
	}
}