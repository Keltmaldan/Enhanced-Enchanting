package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.TagKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.Holder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.AdvancementHolder;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;
import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModMenus;
import net.mcreator.enhancedenchanting.EnhancedEnchantingMod;

public class EnchantmentRemoverBooksRemovesEnchantProcedure {
	public static void execute(LevelAccessor world, Entity entity, double slot) {
		if (entity == null)
			return;
		ItemStack item = ItemStack.EMPTY;
		ItemStack book = ItemStack.EMPTY;
		boolean leg = false;
		MutableComponent final_name = Component.empty();
		String initial = "";
		String search = "";
		TagKey<Enchantment> TAG = TagKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath("enhanced_enchanting", "unremovable"));
		String legEnch = "";
		search = (entity instanceof Player _entity0 && _entity0.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor _menu0) ? _menu0.getMenuState(0, "searchbar", "") : "";
		book = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor _menu1 ? _menu1.getSlots().get((int) slot).getItem() : ItemStack.EMPTY).copy();
		if (entity instanceof Player _player && _player.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor _menu)
			_menu.sendMenuStateUpdate(_player, 0, "searchbar", "", true);
		item = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor _menu3 ? _menu3.getSlots().get(0).getItem() : ItemStack.EMPTY);
		int i = 0;
		if (entity instanceof ServerPlayer _player) {
			AdvancementHolder _adv = _player.server.getAdvancements().get(ResourceLocation.parse("enhanced_enchanting:i_dont_need_this"));
			if (_adv != null) {
				AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
				if (!_ap.isDone()) {
					for (String criteria : _ap.getRemainingCriteria())
						_player.getAdvancements().award(_adv, criteria);
				}
			}
		}
		{
			ItemStack _redwire$itemstack = item;
			ItemEnchantments _appliedEnchantments = _redwire$itemstack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
			for (Holder<Enchantment> _enchantHolder : _appliedEnchantments.keySet()) {
				String stringiterator = world.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getKey(_enchantHolder.value()).toString();
				if (!_enchantHolder.is(TAG) && stringiterator.contains(search)) {
					i++;
					if (i == slot) {
						EnchantmentHelper.updateEnchantments(item, mutableEnchantments -> mutableEnchantments
								.removeIf(enchantment -> enchantment.is(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse(stringiterator))))));
						if (EnhancedEnchantingModVariables.MapVariables.get(world).ModifyNameOfItem) {
							if (EnhancedEnchantingModVariables.legendary.contains(stringiterator)) {
								legEnch = stringiterator;
							}
						}
						EnhancedEnchantingMod.queueServerWork(1, () -> {
							EnchantmentRemoverSlotClearProcedure.execute(entity);
						});
					}
					if (EnhancedEnchantingModVariables.MapVariables.get(world).ModifyNameOfItem) {
						if (EnhancedEnchantingModVariables.legendary.contains(stringiterator) && !(stringiterator).equals(legEnch)) {
							leg = true;
						}
						initial = ((item.getDisplayName().getString()).replace("[", "")).replace("]", "");
						if (!leg) {
							item.set(DataComponents.ITEM_NAME, final_name);
						}
					}
				}
			}
		}
	}
}