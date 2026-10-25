/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.enhancedenchanting.init;

import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.GameRules;

@EventBusSubscriber
public class EnhancedEnchantingModGameRules {
	public static GameRules.Key<GameRules.BooleanValue> DO_LIMIT_LEGENDARY_ENCHANTS_TO_ONE;
	public static GameRules.Key<GameRules.BooleanValue> CHANGE_LEGENDARY_TOOLS_COLOR;
	public static GameRules.Key<GameRules.IntegerValue> ENCHANTMENT_UPGRADER_MAX_LEVEL;
	public static GameRules.Key<GameRules.BooleanValue> DISABLE_FLASHES_FROM_ENHANCED_ENCHANTING;
	public static GameRules.Key<GameRules.BooleanValue> REMOVE_ENCHANTING_TABLE_LIMIT;

	@SubscribeEvent
	public static void registerGameRules(FMLCommonSetupEvent event) {
		DO_LIMIT_LEGENDARY_ENCHANTS_TO_ONE = GameRules.register("doLimitLegendaryEnchantsToOne", GameRules.Category.PLAYER, GameRules.BooleanValue.create(false));
		CHANGE_LEGENDARY_TOOLS_COLOR = GameRules.register("changeLegendaryToolsColor", GameRules.Category.MISC, GameRules.BooleanValue.create(false));
		ENCHANTMENT_UPGRADER_MAX_LEVEL = GameRules.register("enchantmentUpgraderMaxLevel", GameRules.Category.MISC, GameRules.IntegerValue.create(74));
		DISABLE_FLASHES_FROM_ENHANCED_ENCHANTING = GameRules.register("disableFlashesFromEnhancedEnchanting", GameRules.Category.PLAYER, GameRules.BooleanValue.create(false));
		REMOVE_ENCHANTING_TABLE_LIMIT = GameRules.register("removeEnchantingTableLimit", GameRules.Category.MISC, GameRules.BooleanValue.create(true));
	}
}