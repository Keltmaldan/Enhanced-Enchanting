package net.mcreator.enhancedenchanting.potion;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.ParticleOptions;

import net.mcreator.enhancedenchanting.EnhancedEnchantingMod;

public class BrokenArmorMobEffect extends MobEffect {
	public BrokenArmorMobEffect() {
		super(MobEffectCategory.HARMFUL, -1);
		this.withSoundOnAdded(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("item.shield.break")));
		this.addAttributeModifier(Attributes.ARMOR, ResourceLocation.fromNamespaceAndPath(EnhancedEnchantingMod.MODID, "effect.broken_armor_0"), -100, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
	}

	@Override
	public ParticleOptions createParticleOptions(MobEffectInstance mobEffectInstance) {
		return ParticleTypes.CRIT;
	}
}