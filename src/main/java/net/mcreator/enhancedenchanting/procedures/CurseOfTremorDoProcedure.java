package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
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
public class CurseOfTremorDoProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity().level(), event.getEntity());
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}
	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		final LivingEntity living = (LivingEntity) entity;
		final ItemStack item = living.getMainHandItem();
		final Holder<Enchantment> enchant = world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:curse_of_tremor")));
		if (enchant == null)
			return;
		if (item.getEnchantmentLevel(enchant) != 0) {
			final int lvl = item.getEnchantmentLevel(enchant);
			for (int i = -10; i <= 10 * lvl; i++) {
				entity.turn(Math.random() - 0.5, Math.random() - 0.5);
			}
		}
	}
}