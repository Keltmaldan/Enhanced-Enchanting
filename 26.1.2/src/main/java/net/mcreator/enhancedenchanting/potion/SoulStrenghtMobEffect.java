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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModMobEffects;
import net.mcreator.enhancedenchanting.EnhancedEnchantingMod;

@EventBusSubscriber
public class SoulStrenghtMobEffect extends MobEffect {
	public SoulStrenghtMobEffect() {
		super(MobEffectCategory.NEUTRAL, -10092544);
		this.withSoundOnAdded(BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("block.soul_sand.break")));
		this.addAttributeModifier(Attributes.ATTACK_DAMAGE, Identifier.fromNamespaceAndPath(EnhancedEnchantingMod.MODID, "effect.soul_strenght_0"), 0.25, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
		this.addAttributeModifier(Attributes.MOVEMENT_SPEED, Identifier.fromNamespaceAndPath(EnhancedEnchantingMod.MODID, "effect.soul_strenght_1"), 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
		this.addAttributeModifier(Attributes.JUMP_STRENGTH, Identifier.fromNamespaceAndPath(EnhancedEnchantingMod.MODID, "effect.soul_strenght_2"), 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
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

			@Override
			public boolean isVisibleInGui(MobEffectInstance effect) {
				return false;
			}
		}, EnhancedEnchantingModMobEffects.SOUL_STRENGHT.get());
	}
}