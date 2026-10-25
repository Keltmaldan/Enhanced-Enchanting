package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.Holder;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;
import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModMenus;

public class ObsidianGrindStoneGUIWhileThisGUIIsOpenTickProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		ItemStack slot_1 = ItemStack.EMPTY;
		String initial = "";
		boolean leg = false;
		MutableComponent debug = Component.empty();
		MutableComponent name = Component.empty();
		double color = 0;
		double colorStep = 0;
		slot_1 = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor _menu0 ? _menu0.getSlots().get(0).getItem() : ItemStack.EMPTY).copy();
		{
			ItemEnchantments _appliedEnchantments = slot_1.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
			for (Holder<Enchantment> _enchantHolder : _appliedEnchantments.keySet()) {
				String stringiterator = world.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getKey(_enchantHolder.value()).toString();
				if ((entity instanceof Player _entity1 && _entity1.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor _menu1) && _menu1.getMenuState(1, "Mode", false)
						? EnhancedEnchantingModVariables.legendary.contains(stringiterator)
						: stringiterator.contains("curse")) {
					EnchantmentHelper.updateEnchantments(slot_1, mutableEnchantments -> mutableEnchantments
							.removeIf(enchantment -> enchantment.is(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse(stringiterator))))));
					initial = ((slot_1.getDisplayName().getString()).replace("]", "")).replace("[", "");
					slot_1.set(DataComponents.ITEM_NAME, Component.literal(initial));
				}
			}
		}
		if (entity instanceof Player _player && _player.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor _menu) {
			ItemStack _setstack10 = slot_1.copy();
			_setstack10.setCount(1);
			_menu.getSlots().get(1).set(_setstack10);
			_player.containerMenu.broadcastChanges();
		}
	}
}