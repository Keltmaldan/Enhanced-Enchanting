package net.mcreator.enhancedenchanting;

import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.entity.animal.Wolf;
import net.neoforged.neoforge.event.tick.EntityTickEvent.Pre;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.EquipmentSlot;

@EventBusSubscriber
public class RainbowHelper {
	public static void execute(Level level, Entity owner, ItemStack stack) {
		ItemEnchantments enchants = stack.get(DataComponents.ENCHANTMENTS);
		boolean found = false;
		if (enchants != null)
		for (Holder<Enchantment> ench : enchants.keySet()) {
			found = ench.unwrapKey().isPresent() && ench.unwrapKey().get().location().equals(ResourceLocation.fromNamespaceAndPath("enhanced_enchanting", "rainbow"));
			if (found)
				break;
		}
		if (found)
			stack.set(DataComponents.DYED_COLOR, new DyedItemColor(level.getRandom().nextInt(0x1000000), true));
	}
	@SubscribeEvent
	public static void forWolf(Pre event) {
		if (event.getEntity() instanceof Wolf wolf && wolf.hasArmor() && wolf.tickCount % 20 == 0 && !wolf.level().isClientSide())
		execute(wolf.level(), wolf, wolf.getBodyArmorItem());	
		else if (event.getEntity() instanceof ArmorStand armorstand && armorstand.tickCount % 20 == 0 && !armorstand.level().isClientSide()) {
			{
				int i = 0;
				for (EquipmentSlot slot : EquipmentSlot.values()) {
					if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
						execute(armorstand.level(), armorstand, armorstand.getItemBySlot(slot));
						i++;
					}
				}
			}
		}
	}
}