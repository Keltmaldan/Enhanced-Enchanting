package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;

public class RemoveEnchantDoRemoveProcedure {
	public static void execute(LevelAccessor world, CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		double number = 0;
		String enchant = "";
		enchant = "enhanced_enchanting:" + StringArgumentType.getString(arguments, "enchant_id");
		if (EnhancedEnchantingModVariables.WorldVariables.get(world).forbidden.contains(enchant)) {
			while (EnhancedEnchantingModVariables.WorldVariables.get(world).forbidden.size() >= number) {
				if ((EnhancedEnchantingModVariables.WorldVariables.get(world).forbidden.get((int) number) instanceof String _str3 ? _str3 : "").equals(enchant)) {
					EnhancedEnchantingModVariables.WorldVariables.get(world).forbidden.remove((int) number);
					break;
				}
				number = number + 1;
			}
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal((enchant + " enchantment has been re-added to this world")), false);
		} else {
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal((enchant + " enchantment is not disabled")), false);
		}
	}
}