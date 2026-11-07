package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;

import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;

@EventBusSubscriber
public class ShieldBlockingProcedure {
	@SubscribeEvent
	public static void whenEntityBlocksWithShield(LivingShieldBlockEvent event) {
		LivingEntity entity = event.getEntity();
		Entity source = event.getDamageSource().getEntity();
		if (entity == null || source == null)
			return;
		final Holder<Enchantment> fireWall = entity.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:fire_wall")));
		final byte lvl = (byte) entity.getUseItem().getEnchantmentLevel(fireWall);
		if (lvl != 0)
			source.igniteForSeconds(3 * (int) lvl);
	}
}