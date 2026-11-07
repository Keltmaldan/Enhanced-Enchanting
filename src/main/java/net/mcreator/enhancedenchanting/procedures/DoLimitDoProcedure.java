package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;
import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModGameRules;

import javax.annotation.Nullable;

@EventBusSubscriber
public class DoLimitDoProcedure {
	@SubscribeEvent
	public static void onWorldTick(LevelTickEvent.Post event) {
		execute(event, event.getLevel());
	}

	public static void execute(LevelAccessor world) {
		execute(null, world);
	}

	private static void execute(@Nullable Event event, LevelAccessor world) {
		EnhancedEnchantingModVariables.WorldVariables.get(world).DoLimit = world.getLevelData().getGameRules().getBoolean(EnhancedEnchantingModGameRules.DO_LIMIT_LEGENDARY_ENCHANTS_TO_ONE);
		EnhancedEnchantingModVariables.WorldVariables.get(world).markSyncDirty();
	}
}