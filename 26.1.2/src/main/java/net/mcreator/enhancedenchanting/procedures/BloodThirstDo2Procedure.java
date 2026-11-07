
package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.LevelAccessor;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber
public class BloodThirstDo2Procedure {

    private static final ResourceKey<net.minecraft.world.item.enchantment.Enchantment> BLOOD_THIRST = ResourceKey.create(Registries.ENCHANTMENT,Identifier.fromNamespaceAndPath("enhanced_enchanting","blood_thirst"));

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Pre event) {
        execute(event.getEntity().level(),event.getEntity());
    }

    public static void execute(LevelAccessor world,Entity entity) {
        if (!(entity instanceof LivingEntity living)) {
            return;
        }

        if (entity.level().isClientSide()) {
            return;
        }

        ItemStack stack = living.getMainHandItem();

        if (stack.isEmpty()) {
            return;
        }

        var registry = world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);

        var enchantment = registry.get(BLOOD_THIRST).orElse(null);

        if (enchantment == null) {
            return;
        }

        if (stack.getEnchantmentLevel(enchantment) <= 0) {
            return;
        }

        double currentFactor = stack.getOrDefault(DataComponents.CUSTOM_DATA,CustomData.EMPTY).copyTag().getDouble("blood_thirst_factor").orElse(1.0D);

        double newFactor = Math.max(1.0D,currentFactor - 0.005D);

        if (newFactor != currentFactor) {
            CustomData.update(DataComponents.CUSTOM_DATA,stack,tag -> tag.putDouble("blood_thirst_factor",newFactor));
        }

        double multiplier = Math.round(newFactor * 100.0D) / 100.0D;

        if (entity instanceof Player player) {
            player.sendOverlayMessage(Component.literal("\uD83E\uDE78 Blood Thirst : Damages x" + multiplier).withStyle(ChatFormatting.DARK_RED));
        }
    }
}