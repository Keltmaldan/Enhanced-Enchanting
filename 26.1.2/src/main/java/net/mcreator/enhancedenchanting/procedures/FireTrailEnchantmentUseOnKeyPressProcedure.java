package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;

public class FireTrailEnchantmentUseOnKeyPressProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		{
			EnhancedEnchantingModVariables.PlayerVariables _vars = entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES);
			_vars.fire_trail = true;
			_vars.markSyncDirty();
		}
	}
}