package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber
public class LgDebugProcedure {
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        execute(event.getEntity().level(), event.getEntity());
    }

    public static void execute(LevelAccessor world, Entity entity) {
        if (!(entity instanceof Player player) || entity.level().isClientSide()) return;
        var enchantments = world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        var inventory = player.getInventory();

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack.isEmpty()) continue;
            String enchantId = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
                .copyTag().getStringOr("Legendary", "");
            if (enchantId.isEmpty()) continue;
            Identifier id = Identifier.tryParse(enchantId);
            if (id == null) continue;
            var enchantment = enchantments.get(id).orElse(null);
            if (enchantment == null || stack.getEnchantmentLevel(enchantment) == 0) {
                ItemStack updated = stack.copy();
                CustomData.update(DataComponents.CUSTOM_DATA, updated,
                    tag -> tag.putString("Legendary", ""));
                inventory.setItem(i, updated);
            }
        }
    }
}