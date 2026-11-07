package net.mcreator.enhancedenchanting.procedures;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber
public class LegendaryNameRenderProcedure {
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        execute(event.getEntity().level(), event.getEntity());
    }

    public static void execute(LevelAccessor world, Entity entity) {
        if (entity == null || entity.level().isClientSide() || entity.tickCount % 20 != 0) return;
        if (!EnhancedEnchantingModVariables.MapVariables.get(world).ModifyNameOfItem) return;
        var registry = world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        EnhancedEnchantingInventoryHelper.modifyStacks(entity, stack -> {
            if (!stack.isEnchanted()) return stack;
            if (stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
                    .copyTag().getBoolean("has_rendered_name").orElse(false)) return stack;
            ItemEnchantments enchants = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
            boolean legendary = false;
            for (Holder<Enchantment> entry : enchants.keySet()) {
                Identifier id = registry.getKey(entry.value());
                if (id != null && EnhancedEnchantingModVariables.legendary.contains(id.toString())) {
                    legendary = true;
                    break;
                }
            }
            if (!legendary) return stack;
            String rawName = stack.getDisplayName().getString().replace("[", "").replace("]", "");
            stack.set(DataComponents.ITEM_NAME, ReturnLegendaryTextColor3Procedure.execute(rawName));
            CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putBoolean("has_rendered_name", true));
            return stack;
        });
    }
}