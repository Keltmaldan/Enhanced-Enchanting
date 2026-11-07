package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModMobEffects;

public class SoulStrenghtOverlayDisplayOverlayIngameProcedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		return entity instanceof LivingEntity _livEnt0 && _livEnt0.hasEffect(EnhancedEnchantingModMobEffects.SOUL_STRENGHT);
	}
}