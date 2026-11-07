package net.mcreator.enhancedenchanting.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.core.particles.ParticleTypes;

public class InvincibilityMobEffect extends MobEffect {
	public InvincibilityMobEffect() {
		super(MobEffectCategory.BENEFICIAL, -10066432, mobEffectInstance -> ParticleTypes.TOTEM_OF_UNDYING);
	}
}