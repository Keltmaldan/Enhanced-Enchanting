package net.mcreator.enhancedenchanting.procedures;

import java.util.function.UnaryOperator;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public final class EnhancedEnchantingInventoryHelper {
    private EnhancedEnchantingInventoryHelper() {}

    /**
     * The callback receives a COPY and returns the replacement for the same slot.
     */
    public static void modifyStacks(Entity entity, UnaryOperator<ItemStack> change) {
        if (entity == null || entity.level().isClientSide()) return;

        if (entity instanceof Player player) {
            Inventory inventory = player.getInventory();
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                ItemStack original = inventory.getItem(i);
                if (original.isEmpty()) continue;
                ItemStack modified = change.apply(original.copy());
                if (modified != null && !ItemStack.matches(original, modified)) {
                    inventory.setItem(i, modified);
                }
            }
            return;
        }

        ResourceHandler<ItemResource> handler = entity.getCapability(Capabilities.Item.ENTITY, null);
        if (handler != null) {
            for (int i = 0; i < handler.size(); i++) {
                ItemStack original = ItemUtil.getStack(handler, i);
                if (original.isEmpty()) continue;
                ItemStack modified = change.apply(original.copy());
                if (modified == null || ItemStack.matches(original, modified)) continue;

                // Replace atomically, in the SAME slot. If the handler refuses a
                // replacement, the transaction rolls back rather than losing items.
                try (Transaction tx = Transaction.openRoot()) {
                    int removed = handler.extract(i, ItemResource.of(original), original.getCount(), tx);
                    if (removed == original.getCount()) {
                        int inserted = modified.isEmpty() ? 0 : handler.insert(i, ItemResource.of(modified), modified.getCount(), tx);
                        if (inserted == modified.getCount()) {
                            tx.commit();
                        }
                    }
                }
            }
            return;
        }

        if (entity instanceof LivingEntity living) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                ItemStack original = living.getItemBySlot(slot);
                if (original.isEmpty()) continue;
                ItemStack modified = change.apply(original.copy());
                if (modified != null && !ItemStack.matches(original, modified)) {
                    living.setItemSlot(slot, modified);
                }
            }
        }
    }
}