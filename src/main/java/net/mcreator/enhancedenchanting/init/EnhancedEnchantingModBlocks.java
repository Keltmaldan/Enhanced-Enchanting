/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.enhancedenchanting.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredBlock;

import net.minecraft.world.level.block.Block;

import net.mcreator.enhancedenchanting.block.ObsidianGrindStoneBlock;
import net.mcreator.enhancedenchanting.block.LapisLazuliCauldronBlock;
import net.mcreator.enhancedenchanting.block.EnchantmentsUpgraderBlock;
import net.mcreator.enhancedenchanting.block.EnchantmentRemoverBlock;
import net.mcreator.enhancedenchanting.block.EnchanterBlock;
import net.mcreator.enhancedenchanting.EnhancedEnchantingMod;

public class EnhancedEnchantingModBlocks {
	public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(EnhancedEnchantingMod.MODID);
	public static final DeferredBlock<Block> ENCHANTER;
	public static final DeferredBlock<Block> ENCHANTMENTS_UPGRADER;
	public static final DeferredBlock<Block> OBSIDIAN_GRIND_STONE;
	public static final DeferredBlock<Block> LAPIS_LAZULI_CAULDRON;
	public static final DeferredBlock<Block> ENCHANTMENT_REMOVER;
	static {
		ENCHANTER = REGISTRY.register("enchanter", EnchanterBlock::new);
		ENCHANTMENTS_UPGRADER = REGISTRY.register("enchantments_upgrader", EnchantmentsUpgraderBlock::new);
		OBSIDIAN_GRIND_STONE = REGISTRY.register("obsidian_grind_stone", ObsidianGrindStoneBlock::new);
		LAPIS_LAZULI_CAULDRON = REGISTRY.register("lapis_lazuli_cauldron", LapisLazuliCauldronBlock::new);
		ENCHANTMENT_REMOVER = REGISTRY.register("enchantment_remover", EnchantmentRemoverBlock::new);
	}
	// Start of user code block custom blocks
	// End of user code block custom blocks
}