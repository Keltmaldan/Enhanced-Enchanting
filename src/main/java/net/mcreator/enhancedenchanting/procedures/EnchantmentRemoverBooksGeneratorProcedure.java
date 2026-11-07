package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.fml.ModList;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.Holder;

import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModMenus;

public class EnchantmentRemoverBooksGeneratorProcedure {
	public static void execute(LevelAccessor world, Entity entity, double slot) {
		if (entity == null)
			return;
		ItemStack to_give = ItemStack.EMPTY;
		ItemStack item = ItemStack.EMPTY;
		String search = "";
		TagKey<Enchantment> CURSE_TAG = TagKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath("minecraft", "curse"));
		search = (entity instanceof Player _entity0 && _entity0.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor _menu0) ? _menu0.getMenuState(0, "searchbar", "") : "";
		item = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor _menu1 ? _menu1.getSlots().get((int) slot).getItem() : ItemStack.EMPTY);
		if (ModList.get().isLoaded("apotheosis")) {
			item = net.mcreator.enhancedenchanting.ApotheosisGemRemover.removeGems(item);
		}
		int i = 0;
		{
			ItemStack _redwire$itemstack = item;
			ItemEnchantments _appliedEnchantments = _redwire$itemstack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
			for (Holder<Enchantment> _enchantHolder : _appliedEnchantments.keySet()) {
				String stringiterator = world.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getKey(_enchantHolder.value()).toString();
				int lvl = item.getEnchantmentLevel(_enchantHolder);
				ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
				mutable.set(_enchantHolder, lvl);
				if (!_enchantHolder.is(CURSE_TAG) && stringiterator.contains(search)) {
					i++;
					to_give = new ItemStack(Items.ENCHANTED_BOOK);
					to_give.set(DataComponents.STORED_ENCHANTMENTS, mutable.toImmutable());
					if (i <= 27) {
						if (entity instanceof Player _player && _player.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor _menu) {
							ItemStack _setstack4 = to_give;
							_setstack4.setCount(1);
							_menu.getSlots().get((int) i).set(_setstack4);
							_player.containerMenu.broadcastChanges();
						}
					}
				}
			}
		}
	}
}