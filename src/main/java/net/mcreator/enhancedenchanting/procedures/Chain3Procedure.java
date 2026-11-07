package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.entity.Entity;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class Chain3Procedure {
	@SubscribeEvent
	public static void onEntityTick(EntityTickEvent.Pre event) {
		execute(event, event.getEntity());
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		if (entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).chained) {
			{
				EnhancedEnchantingModVariables.PlayerVariables _vars = entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES);
				_vars.chain_cooldown = entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).chain_cooldown - 1;
				_vars.markSyncDirty();
			}
		}
		if (entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).chain_cooldown <= 0) {
			{
				EnhancedEnchantingModVariables.PlayerVariables _vars = entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES);
				_vars.chain_level = 0;
				_vars.chained_by = "";
				_vars.markSyncDirty();
			}
		}
		if (entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).stuck_by_chain) {
			entity.push(((-2) * entity.getDeltaMovement().x()), (entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).chained ? 0 : (-1) * entity.getDeltaMovement().y()), ((-2) * entity.getDeltaMovement().z()));
			{
				EnhancedEnchantingModVariables.PlayerVariables _vars = entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES);
				_vars.stuck_by_chain = false;
				_vars.markSyncDirty();
			}
		}
	}
}