package net.mcreator.enhancedenchanting.procedures;

import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;

import net.mcreator.enhancedenchanting.network.EnhancedEnchantingModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class VelocityDo2Procedure {
	@SubscribeEvent
	public static void onEntityTick(EntityTickEvent.Pre event) {
		execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity());
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double push = 0;
		if (0.2 >= (entity.getDeltaMovement()).length() || entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).velocity_level == 0) {
			return;
		}
		push = Math.log(Math.E + entity.getData(EnhancedEnchantingModVariables.PLAYER_VARIABLES).velocity_level) / 10;
		if (entity.getType().is(TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse("enhanced:arrows")))) {
			if (Blocks.AIR == (world.getBlockState(BlockPos.containing(x, y, z))).getBlock()) {
				entity.push((entity.getDeltaMovement().x() * push), (entity.getDeltaMovement().y() * push), (entity.getDeltaMovement().z() * push));
			}
		}
	}
}