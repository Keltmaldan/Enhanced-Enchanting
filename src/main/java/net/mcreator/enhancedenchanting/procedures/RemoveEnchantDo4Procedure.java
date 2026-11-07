package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.Holder;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class RemoveEnchantDo4Procedure {

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Pre event) {
        execute(event, event.getEntity().level(), event.getEntity());
    }

    public static void execute(LevelAccessor world, Entity entity) {
        execute(null, world, entity);
    }

    private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
        if (entity == null)
            return;

        if (entity.getCapability(Capabilities.ItemHandler.ENTITY, null) instanceof IItemHandlerModifiable handler) {

            for (int i = 0; i < handler.getSlots(); i++) {

                ItemStack stack = handler.getStackInSlot(i);

                // 🔹 On récupère les STORED enchantments (livres)
                ItemEnchantments storedEnchantments =
                        stack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);

                if (!storedEnchantments.isEmpty()) {

                    ItemEnchantments stored =
        stack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);

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

    // 🔹 On remet les stored enchantments modifiés
    stack.set(DataComponents.STORED_ENCHANTMENTS, mutable.toImmutable());
}

                }
            }
        }
    }
}
