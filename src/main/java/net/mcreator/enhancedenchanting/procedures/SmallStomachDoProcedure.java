package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

@EventBusSubscriber
public class SmallStomachDoProcedure {
	@SubscribeEvent
	public static void onUseItemStart(LivingEntityUseItemEvent.Finish event) {
		Player entity = event.getEntity() instanceof Player p ? p : null;
		if (!event.getItem().has(DataComponents.FOOD) || entity == null) return;
		int lvl = entity.getItemBySlot(EquipmentSlot.CHEST).getEnchantmentLevel(
			entity.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(
				ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:small_stomach")
		)));
		if (lvl <= 0) return;
		entity.getFoodData().eat(lvl, 0f);
	}
}