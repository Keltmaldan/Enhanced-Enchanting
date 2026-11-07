package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.ChatFormatting;

public class HelpCommandProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof Player _player4 && !_player4.level().isClientSide())
			_player4.displayClientMessage(withClickEvent(Component.translatable("misc.enhanced_enchanting.help"), ClickEvent.Action.OPEN_URL, "https://discord.gg/twsXK7NDcD").withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.UNDERLINE), false);
	}

	private static MutableComponent withClickEvent(MutableComponent comp, ClickEvent.Action type, String value) {
		return comp.withStyle(_style -> _style.withClickEvent(new ClickEvent(type, value)));
	}
}