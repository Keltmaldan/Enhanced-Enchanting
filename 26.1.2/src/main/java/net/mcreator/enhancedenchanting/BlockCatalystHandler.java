package net.mcreator.enhancedenchanting.procedures;

import net.minecraft.core.HolderLookup.RegistryLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.Enchantment;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber
public class BlockCatalystHandler {
    private static final ResourceKey<Enchantment> CATALYST = ResourceKey.create(Registries.ENCHANTMENT, Identifier.parse("enhanced_enchanting:block_catalyst"));
    private static final String COUNTER = "block_mined";
    private static final int DISPLAY_CAP = 999;

    private static int levelOf(ItemStack item, RegistryLookup<Enchantment> registry) {
        return item.isEmpty() ? 0 : item.getEnchantmentLevel(registry.getOrThrow(CATALYST));
    }

    private static int countOf(ItemStack item) {
        if (item.isEmpty()) return 0;
        	CustomData data = item.get(DataComponents.CUSTOM_DATA);
        if (data == null) return 0;
        double value = data.copyTag().getDoubleOr(COUNTER, 0.0);
        return Double.isFinite(value) ? Math.max(0, Math.min(DISPLAY_CAP, (int) value)) : 0;
    }

    private static void clearCounter(ItemStack item) {
        if (countOf(item) == 0) return;
        	CustomData.update(DataComponents.CUSTOM_DATA, item, tag -> tag.remove(COUNTER));
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        	Inventory inventory = player.getInventory();
        	RegistryLookup<Enchantment> registry = player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        int selectedSlot = inventory.getSelectedSlot();
        	ItemStack held = player.getMainHandItem();
        boolean validHeld = held.is(ItemTags.PICKAXES) && levelOf(held, registry) > 0;

        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack item = inventory.getItem(slot);
            if (item.isEmpty()) continue;
            if (slot == selectedSlot && validHeld) continue;
            if (levelOf(item, registry) > 0 && countOf(item) > 0) {
                clearCounter(item);
            }
        }

        if (validHeld && player.tickCount % 5 == 0) {
            int mined = countOf(held);
            if (mined > 0) {
                player.sendSystemMessage(Component.literal(mined + " blocks mined"), true);
            }
        }
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        ItemStack held = event.getEntity().getMainHandItem();
        if (!held.is(ItemTags.PICKAXES)) return;
        RegistryLookup<Enchantment> registry = event.getEntity().level()
            .registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        int enchantLevel = levelOf(held, registry);
        if (enchantLevel <= 0) return;
        int count = countOf(held);
        if (count <= 0) return;
        double maxBonus = 1.25 * (1.0 + 0.15 * (enchantLevel - 1));
        double bonus = maxBonus * (1.0 - Math.exp(-count / 30.0));
        event.setNewSpeed((float) (event.getNewSpeed() * (1.0 + bonus)));
    }
}