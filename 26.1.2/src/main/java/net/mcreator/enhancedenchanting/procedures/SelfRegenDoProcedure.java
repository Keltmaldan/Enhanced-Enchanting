package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;

@EventBusSubscriber
public class SelfRegenDoProcedure {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();

        if (player.level().isClientSide())
            return;

        var enchantment = player.level().registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(ResourceKey.create(
                        Registries.ENCHANTMENT,
                        Identifier.parse("enhanced_enchanting:self_regen")
                ));

        Inventory inventory = player.getInventory();

        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {

            ItemStack stack = inventory.getItem(slot);

            if (stack.isEmpty() || !stack.isDamageableItem()
                    || !stack.isDamaged())
                continue;

            if (stack.getEnchantmentLevel(enchantment) <= 0)
                continue;

            if (player.getRandom().nextInt(160) != 0)
                continue;

            ItemStack repaired = stack.copy();
            repaired.setDamageValue(
                    Math.max(0, repaired.getDamageValue() - 1)
            );

            inventory.setItem(slot, repaired);
        }
    }
}