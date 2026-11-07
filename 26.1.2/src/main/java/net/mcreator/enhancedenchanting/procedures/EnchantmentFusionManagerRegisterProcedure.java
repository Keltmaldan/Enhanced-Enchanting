package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.resources.Identifier;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;

import net.mcreator.enhancedenchanting.EnchantmentFusionManager;

@EventBusSubscriber
public class EnchantmentFusionManagerRegisterProcedure {
	@SubscribeEvent
public static void register(
    AddServerReloadListenersEvent event
) {
    event.addListener(
        Identifier.fromNamespaceAndPath(
            "enhanced_enchanting", "fusion"
        ),
        new EnchantmentFusionManager()
    );
}
}