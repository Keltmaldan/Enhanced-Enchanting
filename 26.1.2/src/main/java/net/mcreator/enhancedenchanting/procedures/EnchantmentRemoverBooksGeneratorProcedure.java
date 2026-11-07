package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.TagKey;
import net.minecraft.core.registries.Registries;

import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModMenus;

public class EnchantmentRemoverBooksGeneratorProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		ItemStack to_give = ItemStack.EMPTY;
		ItemStack item = ItemStack.EMPTY;
		String search = "";
		TagKey<Enchantment> CURSE_TAG = TagKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath("minecraft", "curse"));
		search = (entity instanceof Player _entity0 && _entity0.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor _menu0) ? _menu0.getMenuState(0, "searchbar", "") : "";
	}
}