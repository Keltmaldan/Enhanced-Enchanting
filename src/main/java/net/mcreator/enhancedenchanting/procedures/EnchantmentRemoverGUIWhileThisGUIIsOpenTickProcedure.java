package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModMenus;

public class EnchantmentRemoverGUIWhileThisGUIIsOpenTickProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		boolean there_are_books = false;
		for (int i = 1; i <= 27; i++) {
			if (Items.ENCHANTED_BOOK == (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor _menu0 ? _menu0.getSlots().get((int) i).getItem() : ItemStack.EMPTY).getItem()) {
				there_are_books = true;
				break;
			}
		}
		if (!((entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor _menu2 ? _menu2.getSlots().get(0).getItem() : ItemStack.EMPTY).getItem() == Blocks.AIR.asItem()
				|| there_are_books)) {
			EnchantmentRemoverBooksGeneratorProcedure.execute(world, entity, 0);
		}
	}
}