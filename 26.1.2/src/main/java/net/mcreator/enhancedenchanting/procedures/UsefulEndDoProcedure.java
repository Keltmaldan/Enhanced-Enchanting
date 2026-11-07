package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;

@EventBusSubscriber
public class UsefulEndDoProcedure {

    private static final Identifier ENCHANTMENT_ID = Identifier.parse("enhanced_enchanting:useful_end");
    private static final Identifier OLD_MODIFIER_ID = Identifier.parse("enhanced_enchanting:useful_end_mining_speed");

    @SubscribeEvent
    public static void onBlockBreakSpeed(PlayerEvent.BreakSpeed event) {

        Player player = event.getEntity();
        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty() || !stack.isDamageableItem())
            return;

        int maxDamage = stack.getMaxDamage();

        if (maxDamage <= 0)
            return;

        var enchantment = player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT,ENCHANTMENT_ID));
        int level = stack.getEnchantmentLevel(enchantment);

        if (level <= 0)
            return;

        double damageRatio = Math.max(0.0,Math.min(1.0,(double) stack.getDamageValue() / maxDamage));
        double multiplier = 1.0 + level * damageRatio;
        event.setNewSpeed((float) (event.getNewSpeed() * multiplier));
    }
    
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {

        Player player = event.getEntity();

        if (player.level().isClientSide())
            return;

        AttributeInstance attribute =
                player.getAttribute(Attributes.MINING_EFFICIENCY);

        if (attribute != null &&
                attribute.hasModifier(OLD_MODIFIER_ID)) {

            attribute.removeModifier(OLD_MODIFIER_ID);
        }
    }
}