package net.mcreator.enhancedenchanting.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

public class EmptyStackItem extends Item {
    public EmptyStackItem(Item.Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
        stack.setCount(0);
    }
	@Override
	public boolean isFoil(ItemStack stack) {
		stack.setCount(0);
		return false;
	}
    
}