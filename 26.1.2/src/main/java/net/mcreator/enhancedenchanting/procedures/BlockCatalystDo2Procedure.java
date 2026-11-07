package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.component.DataComponents;

public class BlockCatalystDo2Procedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		ItemStack main_hand = ItemStack.EMPTY;
		double block_mined = 0;
		if (entity.getCapability(Capabilities.Item.ENTITY, null) instanceof ResourceHandler<ItemResource> _resourceHandlerIter) {
			for (int _idx = 0; _idx < _resourceHandlerIter.size(); _idx++) {
				ItemStack itemstackiterator = ItemUtil.getStack(_resourceHandlerIter, _idx);
				if (itemstackiterator.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, Identifier.parse("enhanced_enchanting:block_catalyst")))) != 0
						&& !(main_hand.getItem() == itemstackiterator.getItem())) {
					{
						final String _tagName = "block_mined";
						final double _tagValue = 0;
						CustomData.update(DataComponents.CUSTOM_DATA, itemstackiterator, tag -> tag.putDouble(_tagName, _tagValue));
					}
				}
			}
		}
		block_mined = main_hand.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr("block_mined", 0);
		if (main_hand.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, Identifier.parse("enhanced_enchanting:block_catalyst")))) != 0 && block_mined != 0) {
			if (entity instanceof ServerPlayer _player)
				_player.sendSystemMessage(Component.literal(((block_mined + " blocks mined").replace(".0", ""))), true);
		}
	}
}