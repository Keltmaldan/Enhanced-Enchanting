/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.enhancedenchanting.init;

import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.mcreator.enhancedenchanting.client.particle.*;

@EventBusSubscriber(Dist.CLIENT)
public class EnhancedEnchantingModParticles {
	@SubscribeEvent
	public static void registerParticles(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(EnhancedEnchantingModParticleTypes.FIRE_AURA_PARTICLE.get(), FireAuraParticleParticle::provider);
		event.registerSpriteSet(EnhancedEnchantingModParticleTypes.SOUL_FIRE_AURA_PARTICLE.get(), SoulFireAuraParticleParticle::provider);
		event.registerSpriteSet(EnhancedEnchantingModParticleTypes.COPPER_FIRE_AURA_PARTICLE.get(), CopperFireAuraParticleParticle::provider);
		event.registerSpriteSet(EnhancedEnchantingModParticleTypes.GRAVITY_PULSE_PARTICLE.get(), GravityPulseParticleParticle::provider);
		event.registerSpriteSet(EnhancedEnchantingModParticleTypes.BACK_PUSHING_PARTICLE.get(), BackPushingParticleParticle::provider);
		event.registerSpriteSet(EnhancedEnchantingModParticleTypes.VAMPIRE_BLOOD_PARTICLE.get(), VampireBloodParticleParticle::provider);
		event.registerSpriteSet(EnhancedEnchantingModParticleTypes.BACK_STAB_PARTICLE.get(), BackStabParticleParticle::provider);
		event.registerSpriteSet(EnhancedEnchantingModParticleTypes.SNOW_BALL_PARTICLE.get(), SnowBallParticleParticle::provider);
		event.registerSpriteSet(EnhancedEnchantingModParticleTypes.FIRE_BALL_PARTICLE.get(), FireBallParticleParticle::provider);
		event.registerSpriteSet(EnhancedEnchantingModParticleTypes.CHAIN_PARTICLE.get(), ChainParticleParticle::provider);
		event.registerSpriteSet(EnhancedEnchantingModParticleTypes.SUN_PARTICLE.get(), SunParticleParticle::provider);
		event.registerSpriteSet(EnhancedEnchantingModParticleTypes.MOON_PARTICLE.get(), MoonParticleParticle::provider);
	}
}