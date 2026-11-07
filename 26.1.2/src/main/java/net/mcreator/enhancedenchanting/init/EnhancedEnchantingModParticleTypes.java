/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.enhancedenchanting.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleType;

import net.mcreator.enhancedenchanting.EnhancedEnchantingMod;

public class EnhancedEnchantingModParticleTypes {
	public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(Registries.PARTICLE_TYPE, EnhancedEnchantingMod.MODID);
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FIRE_AURA_PARTICLE = REGISTRY.register("fire_aura_particle", () -> new SimpleParticleType(true));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SOUL_FIRE_AURA_PARTICLE = REGISTRY.register("soul_fire_aura_particle", () -> new SimpleParticleType(true));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> COPPER_FIRE_AURA_PARTICLE = REGISTRY.register("copper_fire_aura_particle", () -> new SimpleParticleType(true));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GRAVITY_PULSE_PARTICLE = REGISTRY.register("gravity_pulse_particle", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BACK_PUSHING_PARTICLE = REGISTRY.register("back_pushing_particle", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> VAMPIRE_BLOOD_PARTICLE = REGISTRY.register("vampire_blood_particle", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BACK_STAB_PARTICLE = REGISTRY.register("back_stab_particle", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SNOW_BALL_PARTICLE = REGISTRY.register("snow_ball_particle", () -> new SimpleParticleType(true));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FIRE_BALL_PARTICLE = REGISTRY.register("fire_ball_particle", () -> new SimpleParticleType(true));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> CHAIN_PARTICLE = REGISTRY.register("chain_particle", () -> new SimpleParticleType(true));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SUN_PARTICLE = REGISTRY.register("sun_particle", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MOON_PARTICLE = REGISTRY.register("moon_particle", () -> new SimpleParticleType(false));
}