package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber
public class SoulBoundDo2Procedure {
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        execute(event.getEntity().level(), event.getEntity());
    }

    public static void execute(LevelAccessor world, Entity entity) {
        if (!(entity instanceof Player player) || !(world instanceof ServerLevel level)) return;
        var enchant = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
            .get(ResourceKey.create(Registries.ENCHANTMENT,
                Identifier.parse("enhanced_enchanting:soul_bound")))
            .orElse(null);
        if (enchant == null) return;

        var inventory = player.getInventory();
        String currentName = player.getDisplayName().getString();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack.isEmpty() || stack.getEnchantmentLevel(enchant) == 0) continue;

            String owner = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
                .copyTag().getStringOr("boundto", "");
            if (owner.isEmpty()) {
                ItemStack updated = stack.copy();
                CustomData.update(DataComponents.CUSTOM_DATA, updated,
                    tag -> tag.putString("boundto", currentName));
                inventory.setItem(i, updated);
                continue;
            }
            if (owner.equals(currentName)) continue;

            // Preserve the original name-based return-to-owner behavior.
            for (ServerPlayer other : level.players()) {
                if (!owner.equals(other.getDisplayName().getString())) continue;
                ItemStack returned = stack.copy();
                inventory.setItem(i, ItemStack.EMPTY);
                ItemEntity dropped = new ItemEntity(level, other.getX(), other.getY(), other.getZ(), returned);
                dropped.setPickUpDelay(10);
                level.addFreshEntity(dropped);
                return;
            }
        }
    }
}