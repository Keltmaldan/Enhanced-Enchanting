/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.enhancedenchanting.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;

import net.mcreator.enhancedenchanting.item.EmptyStackItem;
import net.mcreator.enhancedenchanting.EnhancedEnchantingMod;

public class EnhancedEnchantingModItems {
	public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(EnhancedEnchantingMod.MODID);
	public static final DeferredItem<Item> ENCHANTER;
	public static final DeferredItem<Item> ENCHANTMENTS_UPGRADER;
	public static final DeferredItem<Item> OBSIDIAN_GRIND_STONE;
	public static final DeferredItem<Item> LAPIS_LAZULI_CAULDRON;
	public static final DeferredItem<Item> ENCHANTMENT_REMOVER;
	public static final DeferredItem<Item> EMPTY_STACK;
	static {
		ENCHANTER = block(EnhancedEnchantingModBlocks.ENCHANTER, new Item.Properties().rarity(Rarity.UNCOMMON));
		ENCHANTMENTS_UPGRADER = block(EnhancedEnchantingModBlocks.ENCHANTMENTS_UPGRADER, new Item.Properties().rarity(Rarity.EPIC));
		OBSIDIAN_GRIND_STONE = block(EnhancedEnchantingModBlocks.OBSIDIAN_GRIND_STONE, new Item.Properties().rarity(Rarity.RARE));
		LAPIS_LAZULI_CAULDRON = block(EnhancedEnchantingModBlocks.LAPIS_LAZULI_CAULDRON, new Item.Properties().rarity(Rarity.UNCOMMON));
		ENCHANTMENT_REMOVER = block(EnhancedEnchantingModBlocks.ENCHANTMENT_REMOVER, new Item.Properties().rarity(Rarity.RARE));
		EMPTY_STACK = REGISTRY.register("empty_stack", EmptyStackItem::new);
	}

	// Start of user code block custom items
	// End of user code block custom items
	private static DeferredItem<Item> block(DeferredHolder<Block, Block> block) {
		return block(block, new Item.Properties());
	}

	private static DeferredItem<Item> block(DeferredHolder<Block, Block> block, Item.Properties properties) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), properties));
	}
}