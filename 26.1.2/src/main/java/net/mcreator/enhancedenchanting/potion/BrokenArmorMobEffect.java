package net.mcreator.enhancedenchanting.potion;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.ParticleTypes;

import net.mcreator.enhancedenchanting.EnhancedEnchantingMod;

public class BrokenArmorMobEffect extends MobEffect {
	public BrokenArmorMobEffect() {
		super(MobEffectCategory.HARMFUL, -1, mobEffectInstance -> ParticleTypes.CRIT);
		this.withSoundOnAdded(BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("item.shield.break")));
		this.addAttributeModifier(Attributes.ARMOR, Identifier.fromNamespaceAndPath(EnhancedEnchantingMod.MODID, "effect.broken_armor_0"), -100, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
	}
}