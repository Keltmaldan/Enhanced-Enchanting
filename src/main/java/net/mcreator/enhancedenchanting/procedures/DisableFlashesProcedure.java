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
public class DisableFlashesProcedure {
	@SubscribeEvent
	public static void onWorldTick(LevelTickEvent.Post event) {
		execute(event, event.getLevel());
	}

	public static void execute(LevelAccessor world) {
		execute(null, world);
	}

	private static void execute(@Nullable Event event, LevelAccessor world) {
		EnhancedEnchantingModVariables.MapVariables.get(world).disable_flashes = world.getLevelData().getGameRules().getBoolean(EnhancedEnchantingModGameRules.DISABLE_FLASHES_FROM_ENHANCED_ENCHANTING);
		EnhancedEnchantingModVariables.MapVariables.get(world).markSyncDirty();
	}
}