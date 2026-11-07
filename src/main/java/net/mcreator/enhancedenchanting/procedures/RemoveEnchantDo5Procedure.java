package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.client.Minecraft;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber(value = {Dist.CLIENT})
public class RemoveEnchantDo5Procedure {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (Minecraft.getInstance().level != null) {
            execute(event, Minecraft.getInstance().level, event.getItemStack());
        }
    }

    public static void execute(LevelAccessor world, ItemStack itemstack) {
        execute(null, world, itemstack);
    }

    private static void execute(@Nullable Event event, LevelAccessor world, ItemStack itemstack) {
        if (world == null || itemstack == null)
            return;

        // 🔹 Récupère les STORED enchantments (livres enchantés)
        ItemEnchantments stored =
                itemstack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);

        if (!stored.isEmpty()) {

            ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(stored);

            mutable.removeIf(enchantment -> {
                ResourceLocation id =
                        world.registryAccess()
                                .registryOrThrow(Registries.ENCHANTMENT)
                                .getKey(enchantment.value());

                if (id == null)
                    return false;

                return EnhancedEnchantingModVariables.WorldVariables
                        .get(world)
                        .forbidden
                        .contains(id.toString());
            });

            // 🔹 Remet les enchantements modifiés dans le stack
            itemstack.set(DataComponents.STORED_ENCHANTMENTS, mutable.toImmutable());
        }
    }
}
