package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.TagKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;

import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModMenus;

public class EnchantmentRemoverBooksRemovesEnchantProcedure {
	public static void execute(Entity entity, double slot) {
		Player player = entity instanceof Player p ? p : null;
		if (player == null)
			return;
		ItemStack item = ItemStack.EMPTY;
		var menu = player.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor _menu0 ? _menu0 : null;
		if (menu == null)
			return;
		boolean leg = false;
		String initial = "";
		TagKey<Enchantment> TAG = TagKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath("enhanced_enchanting", "unremovable"));
		String legEnch = "";
		String search = (player.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor _menu0) ? _menu0.getMenuState(0, "searchbar", "") : "";
		ItemStack book = menu.getSlots().get((int) slot).getItem();
		menu.sendMenuStateUpdate(player, 0, "searchbar", "", true);
	}
}