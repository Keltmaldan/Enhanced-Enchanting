package net.mcreator.enhancedenchanting.procedures;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber
public class RemoveEnchantDo4Procedure {
    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Pre event) {
        execute(event.getEntity().level(), event.getEntity());
    }

    public static void execute(LevelAccessor world, Entity entity) {
        if (entity == null || entity.level().isClientSide()) return;
        var forbidden = EnhancedEnchantingModVariables.WorldVariables.get(world).forbidden;
        if (forbidden == null || forbidden.isEmpty()) return;

        EnhancedEnchantingInventoryHelper.modifyStacks(entity, stack -> {
            ItemEnchantments stored = stack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);
            if (stored.isEmpty()) return stack;
            boolean hasForbidden = stored.keySet().stream().anyMatch(holder ->
                holder.unwrapKey().map(key -> forbidden.contains(key.identifier().toString())).orElse(false));
            if (hasForbidden) {
                ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(stored);
                mutable.removeIf(holder ->
                    holder.unwrapKey().map(key -> forbidden.contains(key.identifier().toString())).orElse(false));
                stack.set(DataComponents.STORED_ENCHANTMENTS, mutable.toImmutable());
            }
            return stack;
        });
    }
}