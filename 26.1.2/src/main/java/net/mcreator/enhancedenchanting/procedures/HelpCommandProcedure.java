package net.mcreator.enhancedenchanting.procedures;

import java.net.URI;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class HelpCommandProcedure {
    public static void execute(Entity entity) {
        if (!(entity instanceof Player player) || player.level().isClientSide()) return;
        player.sendSystemMessage(
            Component.translatable("misc.enhanced_enchanting.help")
                .withStyle(ChatFormatting.BLUE, ChatFormatting.UNDERLINE)
                .withStyle(style -> style.withClickEvent(
                    new ClickEvent.OpenUrl(URI.create("https://discord.gg/twsXK7NDcD"))
                ))
        );
    }
}