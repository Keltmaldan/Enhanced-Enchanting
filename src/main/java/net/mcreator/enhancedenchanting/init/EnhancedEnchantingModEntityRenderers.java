/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.enhancedenchanting.init;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.mcreator.enhancedenchanting.client.renderer.ShockwaveProjectileRenderer;

@EventBusSubscriber(Dist.CLIENT)
public class EnhancedEnchantingModEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(EnhancedEnchantingModEntities.SHOCKWAVE_PROJECTILE.get(), ShockwaveProjectileRenderer::new);
	}
}