package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModMenus;

public class EnchantmentRemoverSlotClearProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		for (int i = 1; i <= 27; i++) {
			if (entity instanceof Player _player && _player.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor _menu) {
				_menu.getSlots().get((int) i).set(ItemStack.EMPTY);
				_player.containerMenu.broadcastChanges();
			}
		}
	}
}