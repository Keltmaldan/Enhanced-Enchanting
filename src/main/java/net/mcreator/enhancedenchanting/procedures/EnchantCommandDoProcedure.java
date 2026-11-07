package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionHand;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Holder;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;

public class EnchantCommandDoProcedure {
	public static void execute(LevelAccessor world, CommandContext<CommandSourceStack> arguments) {
		if ((commandParameterEntity(arguments, "entity")) instanceof LivingEntity _entity) {
			ItemStack _setstack6 = (EnchantmentHelper.enchantItem(world.getRandom(), ((commandParameterEntity(arguments, "entity")) instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY),
					(int) DoubleArgumentType.getDouble(arguments, "level"),
					((BoolArgumentType.getBool(arguments, "treasure")))
							? world.registryAccess().registryOrThrow(Registries.ENCHANTMENT).holders().map(reference -> (Holder<Enchantment>) reference)
							: world.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getTag(EnchantmentTags.IN_ENCHANTING_TABLE).get().stream()))
					.copy();
			_setstack6.setCount(1);
			_entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack6);
			if (_entity instanceof Player _player)
				_player.getInventory().setChanged();
		}
	}

	private static Entity commandParameterEntity(CommandContext<CommandSourceStack> arguments, String parameter) {
		try {
			return EntityArgument.getEntity(arguments, parameter);
		} catch (CommandSyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}
}