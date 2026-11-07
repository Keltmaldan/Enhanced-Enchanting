package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;

import javax.annotation.Nullable;

import java.util.ArrayList;

@EventBusSubscriber
public class RestorativeSleepDayProcedure {
	@SubscribeEvent
	public static void onWorldTick(LevelTickEvent.Post event) {
		execute(event, event.getLevel());
	}

	public static void execute(LevelAccessor world) {
		execute(null, world);
	}

	private static void execute(@Nullable Event event, LevelAccessor world) {
		if (world instanceof Level _lvl0 && _lvl0.isDay()) {
			for (Entity entityiterator : new ArrayList<>(world.players())) {
				{
					EnhancedEnchantingModVariables.PlayerVariables _vars = entityiterator.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES);
					_vars.AlreadySlept = false;
					_vars.markSyncDirty();
				}
			}
		}
	}
}