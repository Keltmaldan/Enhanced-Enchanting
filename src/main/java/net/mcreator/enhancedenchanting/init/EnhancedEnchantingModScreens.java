/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.enhancedenchanting.init;

import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.mcreator.enhancedenchanting.client.gui.ObsidianGrindStoneGUIScreen;
import net.mcreator.enhancedenchanting.client.gui.EnchantmentsUpgraderGUIScreen;
import net.mcreator.enhancedenchanting.client.gui.EnchantmentRemoverGUIScreen;
import net.mcreator.enhancedenchanting.client.gui.EnchanterGUIScreen;

@EventBusSubscriber(Dist.CLIENT)
public class EnhancedEnchantingModScreens {
	@SubscribeEvent
	public static void clientLoad(RegisterMenuScreensEvent event) {
		event.register(EnhancedEnchantingModMenus.ENCHANTER_GUI.get(), EnchanterGUIScreen::new);
		event.register(EnhancedEnchantingModMenus.ENCHANTMENTS_UPGRADER_GUI.get(), EnchantmentsUpgraderGUIScreen::new);
		event.register(EnhancedEnchantingModMenus.OBSIDIAN_GRIND_STONE_GUI.get(), ObsidianGrindStoneGUIScreen::new);
		event.register(EnhancedEnchantingModMenus.ENCHANTMENT_REMOVER_GUI.get(), EnchantmentRemoverGUIScreen::new);
	}

	public interface ScreenAccessor {
		void updateMenuState(int elementType, String name, Object elementState);
	}
}