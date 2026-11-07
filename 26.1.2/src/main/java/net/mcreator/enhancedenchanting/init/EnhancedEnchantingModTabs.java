/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.enhancedenchanting.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

import net.mcreator.enhancedenchanting.EnhancedEnchantingMod;

@EventBusSubscriber
public class EnhancedEnchantingModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EnhancedEnchantingMod.MODID);
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ENHANCED_ENCHANTING_TAB = REGISTRY.register("enhanced_enchanting_tab",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.enhanced_enchanting.enhanced_enchanting_tab")).icon(() -> new ItemStack(Items.ENCHANTED_BOOK)).displayItems((parameters, tabData) -> {
				tabData.accept(EnhancedEnchantingModBlocks.ENCHANTER.get().asItem());
				tabData.accept(EnhancedEnchantingModBlocks.ENCHANTMENTS_UPGRADER.get().asItem());
				tabData.accept(EnhancedEnchantingModBlocks.OBSIDIAN_GRIND_STONE.get().asItem());
				tabData.accept(EnhancedEnchantingModBlocks.LAPIS_LAZULI_CAULDRON.get().asItem());
				tabData.accept(EnhancedEnchantingModBlocks.ENCHANTMENT_REMOVER.get().asItem());
			}).withSearchBar().build());

	@SubscribeEvent
	public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
		if (tabData.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
			tabData.accept(EnhancedEnchantingModBlocks.ENCHANTER.get().asItem());
			tabData.accept(EnhancedEnchantingModBlocks.ENCHANTMENTS_UPGRADER.get().asItem());
			tabData.accept(EnhancedEnchantingModBlocks.OBSIDIAN_GRIND_STONE.get().asItem());
			tabData.accept(EnhancedEnchantingModBlocks.LAPIS_LAZULI_CAULDRON.get().asItem());
			tabData.accept(EnhancedEnchantingModBlocks.ENCHANTMENT_REMOVER.get().asItem());
		}
	}
}