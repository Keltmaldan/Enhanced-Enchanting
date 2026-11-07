/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.enhancedenchanting.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredBlock;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;

import net.mcreator.enhancedenchanting.block.ObsidianGrindStoneBlock;
import net.mcreator.enhancedenchanting.block.LapisLazuliCauldronBlock;
import net.mcreator.enhancedenchanting.block.EnchantmentsUpgraderBlock;
import net.mcreator.enhancedenchanting.block.EnchantmentRemoverBlock;
import net.mcreator.enhancedenchanting.block.EnchanterBlock;
import net.mcreator.enhancedenchanting.EnhancedEnchantingMod;

import java.util.function.Function;

public class EnhancedEnchantingModBlocks {
	public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(EnhancedEnchantingMod.MODID);
	public static final DeferredBlock<Block> ENCHANTER;
	public static final DeferredBlock<Block> ENCHANTMENTS_UPGRADER;
	public static final DeferredBlock<Block> OBSIDIAN_GRIND_STONE;
	public static final DeferredBlock<Block> LAPIS_LAZULI_CAULDRON;
	public static final DeferredBlock<Block> ENCHANTMENT_REMOVER;
	static {
		ENCHANTER = register("enchanter", EnchanterBlock::new);
		ENCHANTMENTS_UPGRADER = register("enchantments_upgrader", EnchantmentsUpgraderBlock::new);
		OBSIDIAN_GRIND_STONE = register("obsidian_grind_stone", ObsidianGrindStoneBlock::new);
		LAPIS_LAZULI_CAULDRON = register("lapis_lazuli_cauldron", LapisLazuliCauldronBlock::new);
		ENCHANTMENT_REMOVER = register("enchantment_remover", EnchantmentRemoverBlock::new);
	}

	// Start of user code block custom blocks
	// End of user code block custom blocks
	private static <B extends Block> DeferredBlock<B> register(String name, Function<BlockBehaviour.Properties, ? extends B> supplier) {
		return REGISTRY.registerBlock(name, supplier);
	}
}