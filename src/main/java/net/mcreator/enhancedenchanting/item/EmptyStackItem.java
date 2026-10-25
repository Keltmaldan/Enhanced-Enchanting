package net.mcreator.enhancedenchanting.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;

public class EmptyStackItem extends Item {
	public EmptyStackItem() {
		super(new Item.Properties().stacksTo(1));
	}

	@Override
	public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
		itemstack.setCount(0);
	}
}