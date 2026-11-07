package net.mcreator.enhancedenchanting;

import net.neoforged.neoforge.event.entity.EntityEvent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.minecraft.world.entity.Entity;

@EventBusSubscriber
public class EnchantmentFusionEvent extends EntityEvent implements ICancellableEvent {
	private ItemStack itemstack;
	private final EnchantmentFusion fusion;
	public EnchantmentFusionEvent(ItemStack i, Entity ent, EnchantmentFusion fus) {
		super(ent);
		this.itemstack = i;
		this.fusion = fus;

	}

	public EnchantmentFusion getFusion() {
		return this.fusion;
	}

	public ItemStack getItemStack() {
		return this.itemstack;
	}

	public void setItemStack(ItemStack newStack) {
		this.itemstack = newStack;
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void trigger(EnchantmentFusionEvent event) {
		if (!event.isCanceled())
			event.getFusion().trigger(event.getItemStack(), event.getEntity());
	}
}
