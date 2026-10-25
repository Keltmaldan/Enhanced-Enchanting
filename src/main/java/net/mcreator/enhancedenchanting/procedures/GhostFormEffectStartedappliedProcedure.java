package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.Minecraft;

import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModMobEffects;

import javax.annotation.Nullable;

@EventBusSubscriber(value = Dist.CLIENT)
public class GhostFormEffectStartedappliedProcedure {
	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();
		execute(event, mc.player);
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof LivingEntity _livEnt0 && _livEnt0.hasEffect(EnhancedEnchantingModMobEffects.GHOST_FORM)) {
			if (!(Minecraft.getInstance().gameRenderer.currentEffect() != null)) {
				Minecraft.getInstance().gameRenderer.loadEffect(ResourceLocation.parse("enhanced_enchanting:shaders/ghost.json"));
			}
		} else if (Minecraft.getInstance().gameRenderer.currentEffect() == null ? false : Minecraft.getInstance().gameRenderer.currentEffect().getName().equals("enhanced_enchanting:shaders/ghost.json")) {
			Minecraft.getInstance().gameRenderer.shutdownEffect();
		}
	}
}