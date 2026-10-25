package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

import net.mcreator.enhancedenchanting.EnchantmentFusionManager;

@EventBusSubscriber
public class EnchantmentFusionManagerRegisterProcedure {
	@SubscribeEvent
	public static void execute(AddReloadListenerEvent event) {
		event.addListener(new EnchantmentFusionManager());
	}
}