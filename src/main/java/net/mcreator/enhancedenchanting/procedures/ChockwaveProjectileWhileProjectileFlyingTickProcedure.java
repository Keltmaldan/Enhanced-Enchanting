package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.world.entity.Entity;

public class ChockwaveProjectileWhileProjectileFlyingTickProcedure {
	public static void execute(Entity immediatesourceentity) {
		if (immediatesourceentity == null)
			return;
		if (immediatesourceentity.tickCount >= 50) {
			if (!immediatesourceentity.level().isClientSide())
				immediatesourceentity.discard();
		}
	}
}