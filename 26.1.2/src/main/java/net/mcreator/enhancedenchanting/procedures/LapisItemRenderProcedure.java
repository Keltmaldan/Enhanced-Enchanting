package net.mcreator.enhancedenchanting.procedures;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber(value = Dist.CLIENT)
public class LapisItemRenderProcedure {
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (Minecraft.getInstance().level != null) {
            execute(Minecraft.getInstance().level, event.getItemStack(), event.getToolTip());
        }
    }

    public static void execute(LevelAccessor world, ItemStack stack, List<Component> tooltip) {
        if (world == null || stack == null || stack.isEmpty() || tooltip == null) return;

        String enchantId = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
            .copyTag().getStringOr("lapis_enchant", "");
        if (enchantId.isEmpty()) return;
        Identifier parsedId = Identifier.tryParse(enchantId);
        if (parsedId == null) return;

        var registry = world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        Holder.Reference<Enchantment> enchantment = registry.get(parsedId).orElse(null);
        if (enchantment == null) return;

        String translatedName = Component.translatable("enchantment." + enchantId.replace(':', '.')).getString();
        int appliedLevel = stack.getEnchantmentLevel(enchantment);
        String suffix = enchantment.value().getMaxLevel() != 1 && appliedLevel > 0
            ? " " + Component.translatable("enchantment.level." + appliedLevel).getString()
            : "";
        Component replacement = Component.literal("Lapis Lazuli : " + translatedName + suffix)
            .withStyle(ChatFormatting.AQUA);

        // Don't mutate ItemStack data in a client-side tooltip event.
        for (int i = tooltip.size() - 1; i >= 0; i--) {
            String line = tooltip.get(i).getString().replace(" ", "");
            if (line.startsWith(translatedName.replace(" ", ""))) {
                tooltip.set(i, replacement);
                return;
            }
        }
        tooltip.add(replacement);
    }
}