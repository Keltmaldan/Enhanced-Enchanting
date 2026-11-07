/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.enhancedenchanting.init;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.mcreator.enhancedenchanting.client.model.Modelchockwave;

@EventBusSubscriber(Dist.CLIENT)
public class EnhancedEnchantingModModels {
	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(Modelchockwave.LAYER_LOCATION, Modelchockwave::createBodyLayer);
	}
}