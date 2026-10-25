package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;

public class RemoveEnchantDoProcedure {
	public static void execute(LevelAccessor world, CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		double number = 0;
		String enchant = "";
		enchant = "enhanced_enchanting:" + StringArgumentType.getString(arguments, "enchant_id");
		EnhancedEnchantingModVariables.WorldVariables.get(world).forbidden.add(enchant);
		EnhancedEnchantingModVariables.WorldVariables.get(world).markSyncDirty();
		if (entity instanceof Player _player && !_player.level().isClientSide())
			_player.displayClientMessage(Component.literal((enchant + " enchantment has been removed from this world")), false);
	}
}