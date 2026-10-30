package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

public class EditConfigCommandProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		try {
			net.mcreator.enhancedenchanting.EnchantmentModificationReader.executeConfig();
		} catch (Exception e) {
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal(e.getStackTrace().toString()), false);
		}
	}
}