package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;

@EventBusSubscriber
public class AerodynamismDoProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity());
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double push = 0;
		ItemStack elytra = ItemStack.EMPTY;
		if (entity.tickCount % 2 != 0)
			return;
		if (!(entity instanceof LivingEntity _livEnt0 && _livEnt0.isFallFlying())) {
			return;
		}
		elytra = GetElytraProcedure.execute(entity).copy();
		if (!(elytra.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:aerodynamism")))) != 0)) {
			return;
		}
		push = Math.log(Math.E + elytra.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("enhanced_enchanting:aerodynamism"))))) / 30;
		if (Blocks.AIR == (world.getBlockState(BlockPos.containing(x, y, z))).getBlock()) {
			entity.push((entity.getDeltaMovement().x() * push), (entity.getDeltaMovement().y() * push), (entity.getDeltaMovement().z() * push));
		}
	}
}