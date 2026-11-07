/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.enhancedenchanting.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.core.registries.Registries;

import net.mcreator.enhancedenchanting.EnhancedEnchantingMod;

import com.mojang.serialization.Codec;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;

public class EnhancedEnchantingModGameRules {
	public static final DeferredRegister<GameRule<?>> REGISTRY = DeferredRegister.create(Registries.GAME_RULE, EnhancedEnchantingMod.MODID);
	public static DeferredHolder<GameRule<?>, GameRule<Boolean>> DO_LIMIT_LEGENDARY_ENCHANTS_TO_ONE = registerBoolean("do_limit_legendary_enchants_to_one", GameRuleCategory.PLAYER, false);
	public static DeferredHolder<GameRule<?>, GameRule<Boolean>> CHANGE_LEGENDARY_TOOLS_COLOR = registerBoolean("change_legendary_tools_color", GameRuleCategory.MISC, false);
	public static DeferredHolder<GameRule<?>, GameRule<Integer>> ENCHANTMENT_UPGRADER_MAX_LEVEL = registerInteger("enchantment_upgrader_max_level", GameRuleCategory.MISC, 74);
	public static DeferredHolder<GameRule<?>, GameRule<Boolean>> DISABLE_FLASHES_FROM_ENHANCED_ENCHANTING = registerBoolean("disable_flashes_from_enhanced_enchanting", GameRuleCategory.PLAYER, false);
	public static DeferredHolder<GameRule<?>, GameRule<Boolean>> REMOVE_ENCHANTING_TABLE_LIMIT = registerBoolean("remove_enchanting_table_limit", GameRuleCategory.MISC, true);

	private static DeferredHolder<GameRule<?>, GameRule<Boolean>> registerBoolean(String registryname, GameRuleCategory category, boolean value) {
		return REGISTRY.register(registryname, () -> new GameRule<>(category, GameRuleType.BOOL, BoolArgumentType.bool(), GameRuleTypeVisitor::visitBoolean, Codec.BOOL, b -> b ? 1 : 0, value, FeatureFlagSet.of()));
	}

	private static DeferredHolder<GameRule<?>, GameRule<Integer>> registerInteger(String registryname, GameRuleCategory category, int value) {
		return REGISTRY.register(registryname, () -> new GameRule<>(category, GameRuleType.INT, IntegerArgumentType.integer(Integer.MIN_VALUE, Integer.MAX_VALUE), GameRuleTypeVisitor::visitInteger,
				Codec.intRange(Integer.MIN_VALUE, Integer.MAX_VALUE), i -> i, value, FeatureFlagSet.of()));
	}
}