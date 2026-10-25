/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.enhancedenchanting.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.core.registries.Registries;

import net.mcreator.enhancedenchanting.potion.SoulStrenghtMobEffect;
import net.mcreator.enhancedenchanting.potion.InvincibilityMobEffect;
import net.mcreator.enhancedenchanting.potion.GhostFormMobEffect;
import net.mcreator.enhancedenchanting.potion.FearlessEffectMobEffect;
import net.mcreator.enhancedenchanting.potion.BrokenArmorMobEffect;
import net.mcreator.enhancedenchanting.EnhancedEnchantingMod;

public class EnhancedEnchantingModMobEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(Registries.MOB_EFFECT, EnhancedEnchantingMod.MODID);
	public static final DeferredHolder<MobEffect, MobEffect> SOUL_STRENGHT = REGISTRY.register("soul_strenght", SoulStrenghtMobEffect::new);
	public static final DeferredHolder<MobEffect, MobEffect> FEARLESS_EFFECT = REGISTRY.register("fearless_effect", FearlessEffectMobEffect::new);
	public static final DeferredHolder<MobEffect, MobEffect> BROKEN_ARMOR = REGISTRY.register("broken_armor", BrokenArmorMobEffect::new);
	public static final DeferredHolder<MobEffect, MobEffect> INVINCIBILITY = REGISTRY.register("invincibility", InvincibilityMobEffect::new);
	public static final DeferredHolder<MobEffect, MobEffect> GHOST_FORM = REGISTRY.register("ghost_form", GhostFormMobEffect::new);
}