package net.mcreator.enhancedenchanting.potion;

import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.extensions.common.IClientMobEffectExtensions;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.resources.Identifier;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModMobEffects;
import net.mcreator.enhancedenchanting.EnhancedEnchantingMod;

@EventBusSubscriber
public class FearlessEffectMobEffect extends MobEffect {
	public FearlessEffectMobEffect() {
		super(MobEffectCategory.NEUTRAL, -9809838, mobEffectInstance -> ParticleTypes.LAVA);
		this.addAttributeModifier(Attributes.ATTACK_DAMAGE, Identifier.fromNamespaceAndPath(EnhancedEnchantingMod.MODID, "effect.fearless_effect_0"), 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
		this.addAttributeModifier(Attributes.MAX_HEALTH, Identifier.fromNamespaceAndPath(EnhancedEnchantingMod.MODID, "effect.fearless_effect_1"), -0.25, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
	}

	@SubscribeEvent
	public static void registerMobEffectExtensions(RegisterClientExtensionsEvent event) {
		event.registerMobEffect(new IClientMobEffectExtensions() {
			@Override
			public boolean isVisibleInInventory(MobEffectInstance effect) {
				return false;
			}

			@Override
			public boolean renderInventoryText(MobEffectInstance instance, AbstractContainerScreen<?> screen, GuiGraphicsExtractor guiGraphics, int x, int y, int blitOffset) {
				return false;
			}
		}, EnhancedEnchantingModMobEffects.FEARLESS_EFFECT.get());
	}
}