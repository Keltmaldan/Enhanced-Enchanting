package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.GameType;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.Holder;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.Minecraft;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;
import net.mcreator.enhancedenchanting.init.EnhancedEnchantingModMenus;
import net.minecraft.world.item.Items;
import java.util.Map;
import net.minecraft.world.inventory.Slot;
import net.minecraft.core.component.DataComponentType;

public class EnchantmentsUpgraderGUIButtonDoProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		int max_reached = 0;
		Player player = entity instanceof Player plr ? plr : null;
		if (player == null)
			return;
		Map<Integer, Slot> slots = player.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor m ? m.getSlots() : null;
		if (slots == null)
			return;
		ItemStack item = slots.get(0).getItem();
		DataComponentType<ItemEnchantments> components;
		if (item.is(Items.ENCHANTED_BOOK))
			components = DataComponents.STORED_ENCHANTMENTS;
		else if (item.isEnchanted())
			components = DataComponents.ENCHANTMENTS;
		else return;
		
		{
			ItemEnchantments enchants = item.copy().getOrDefault(components, ItemEnchantments.EMPTY);
			for (Holder<Enchantment> enchant : enchants.keySet()) {
				int lvl = enchants.getLevel(enchant);
				if (!(world.isClientSide() || lvl >= EnhancedEnchantingModVariables.MapVariables.get(world).max_upgrader_level)) {
					int maxLevel = enchant.value().getMaxLevel();
					if ((getAmountInGUISlot(entity, 2) + 10 > lvl || getEntityGameType(entity) == GameType.CREATIVE) && maxLevel != 1) {
						lvl++;
						EnchantmentHelper.updateEnchantments(item, mutableEnchantments -> mutableEnchantments.removeIf(enchantment -> enchantment.is(enchant)));
						item.enchant(enchant, lvl);
						max_reached = Math.max(lvl - 1, max_reached);
					}
				}
			}
		}
		slots.get(2).remove(Math.max(0, max_reached - 10));
		player.containerMenu.broadcastChanges();
	}

	private static int getAmountInGUISlot(Entity entity, int sltid) {
		if (entity instanceof Player player && player.containerMenu instanceof EnhancedEnchantingModMenus.MenuAccessor menuAccessor) {
			ItemStack stack = menuAccessor.getSlots().get(sltid).getItem();
			if (stack != null)
				return stack.getCount();
		}
		return 0;
	}

	private static GameType getEntityGameType(Entity entity) {
		if (entity instanceof ServerPlayer serverPlayer) {
			return serverPlayer.gameMode.getGameModeForPlayer();
		} else if (entity instanceof Player player && player.level().isClientSide()) {
			PlayerInfo playerInfo = Minecraft.getInstance().getConnection().getPlayerInfo(player.getGameProfile().getId());
			if (playerInfo != null)
				return playerInfo.getGameMode();
		}
		return null;
	}
}