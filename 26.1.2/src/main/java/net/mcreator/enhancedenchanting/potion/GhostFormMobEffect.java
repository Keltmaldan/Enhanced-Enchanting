package net.mcreator.enhancedenchanting.potion;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.resources.Identifier;

import net.mcreator.enhancedenchanting.procedures.GhostFormEffectStartedappliedProcedure;
import net.mcreator.enhancedenchanting.EnhancedEnchantingMod;

public class GhostFormMobEffect extends MobEffect {
	public GhostFormMobEffect() {
		super(MobEffectCategory.BENEFICIAL, -1);
		this.addAttributeModifier(Attributes.MOVEMENT_SPEED, Identifier.fromNamespaceAndPath(EnhancedEnchantingMod.MODID, "effect.ghost_form_0"), 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
	}

	@Override
	public void onEffectStarted(LivingEntity entity, int amplifier) {
		GhostFormEffectStartedappliedProcedure.execute(entity);
	}
}