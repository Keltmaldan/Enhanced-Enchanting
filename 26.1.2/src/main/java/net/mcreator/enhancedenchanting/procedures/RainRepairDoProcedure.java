package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber
public class RainRepairDoProcedure {
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        execute(event.getEntity().level(), event.getEntity());
    }

    public static void execute(LevelAccessor world, Entity entity) {
        if (entity == null || entity.level().isClientSide() || entity.tickCount % 60 != 0) return;
        if (!entity.isInWaterOrRain() || entity.isInWater()) return;
        var enchant = world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(ResourceKey.create(Registries.ENCHANTMENT,
                    Identifier.parse("enhanced_enchanting:rain_repair")));
        EnhancedEnchantingInventoryHelper.modifyStacks(entity, stack -> {
            if (stack.isDamaged() && stack.getEnchantmentLevel(enchant) > 0) {
                stack.setDamageValue(Math.max(0, stack.getDamageValue() - 1));
            }
            return stack;
        });
    }
}