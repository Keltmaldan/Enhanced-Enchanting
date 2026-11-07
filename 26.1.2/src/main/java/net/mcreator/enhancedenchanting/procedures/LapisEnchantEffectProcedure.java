package net.mcreator.enhancedenchanting.procedures;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber
public class LapisEnchantEffectProcedure {
    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Pre event) {
        execute(event.getEntity().level(), event.getEntity());
    }

    public static void execute(LevelAccessor world, Entity entity) {
        if (entity == null || entity.level().isClientSide()) return;
        var enchantments = world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        boolean renameItems = EnhancedEnchantingModVariables.MapVariables.get(world).ModifyNameOfItem;

        EnhancedEnchantingInventoryHelper.modifyStacks(entity, stack -> {
            String name = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
                .copyTag().getStringOr("lapis_enchant", "");
            if (name.isEmpty()) return stack;

            Identifier id = Identifier.tryParse(name);
            if (id == null) return stack;
            Holder.Reference<Enchantment> enchantment = enchantments.get(id).orElse(null);
            if (enchantment == null) return stack;

            if (stack.getEnchantmentLevel(enchantment) == 0) {
                stack.enchant(enchantment, 1);
            }

            if (renameItems && !stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
                    .copyTag().getBooleanOr("has_rendered_name", false)) {
                String originalName = stack.getHoverName().getString();
                if (!originalName.isEmpty()) {
                    MutableComponent gradient = Component.empty();
                    int lastIndex = Math.max(1, originalName.length() - 1);
                    for (int index = 0; index < originalName.length(); index++) {
                        float t = (float) index / lastIndex;
                        int r = (int) (0x59 + (0x2D - 0x59) * t);
                        int g = (int) (0x76 + (0x3E - 0x76) * t);
                        int b = (int) (0xBE + (0x78 - 0xBE) * t);
                        int rgb = (r << 16) | (g << 8) | b;
                        gradient.append(Component.literal(originalName.substring(index, index + 1)).withColor(rgb));
                    }
                    stack.set(DataComponents.ITEM_NAME, gradient);
                    CustomData.update(DataComponents.CUSTOM_DATA, stack,
                        tag -> tag.putBoolean("has_rendered_name", true));
                }
            }
            return stack;
        });
    }
}